package com.tgerstel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;
import com.tgerstel.repository.TransferRepository;

@Service
public class TransferService {
	
	private final TransferRepository transferRepo;
	private final ReceiptService receiptService;

	public TransferService(TransferRepository transferRepo, ReceiptService receiptService) {
		super();
		this.transferRepo = transferRepo;
		this.receiptService = receiptService;
	}
	
	public Optional<Transfer> createTransfer(Transfer transfer, User user) {
		
		transfer.setUser(user);
		Optional<Receipt> receipt = receiptService.getById(user, transfer.getReceipt().getId());
		if(receipt.isEmpty()) return Optional.empty();
		return Optional.of(transferRepo.save(transfer));		
	}
	
	public List<Transfer> getRecentTransfers(User user, Integer resultSize) {
		
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("date").descending());		
		return transferRepo.findAllByUser(user, page);
	}

	public Optional<Transfer> getById(User user, Long id) {
		
		Optional<Transfer> result = transferRepo.findById(id);		
		if (result.isPresent() && currentUserOwnsTransfer(user, result)) return result;
		return Optional.empty();
	}
	
	public void deleteTransfer(User user, Long id) {
		
		Optional<Transfer> deletingTransfer = transferRepo.findById(id);		
		if(deletingTransfer.isPresent() && currentUserOwnsTransfer(user, deletingTransfer)) {
			transferRepo.deleteById(id);
		}
	}

	//test
	boolean currentUserOwnsTransfer(User user, Optional<Transfer> transfer) {
		
		if(transfer.get().getUser().getId() == user.getId()) return true;
		return false;
	}

	public Optional<Transfer> getByReceipt(Optional<Receipt> receipt) {

		Optional<Transfer> transfer = transferRepo.findByReceipt(receipt);
		if(transfer.isEmpty()) return Optional.empty();		
		return transfer;		
	}
	
	public List<Long> getAllReceiptsIdInTransfers() {		
		
		List<Transfer> allTransfers = transferRepo.findAllByUser();
		return allTransfers.stream().map(t -> t.getReceipt().getId()).toList();		
		
	}
	
	//test
	private Optional<Transfer> completeTransferDataFromReceipt(Transfer transfer) {		
		
		if(transfer.getReceipt() == null) return Optional.of(transfer);
		if(transfer.getDate() == null) transfer.setDate(transfer.getReceipt().getDate());
		if(transfer.getDescription() == null) transfer.setDescription(transfer.getReceipt().getDescription());
		if(transfer.getFrom() == null) transfer.setFrom(transfer.getReceipt().getClient());
		if(transfer.getTo() == null) transfer.setTo(transfer.getReceipt().getWorker());
		return Optional.of(transfer);
	}
	
}










