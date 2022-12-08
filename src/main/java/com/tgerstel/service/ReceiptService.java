package com.tgerstel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;

@Service
public class ReceiptService {
	
	private final ReceiptRepository receiptRepo;

	
	public ReceiptService(ReceiptRepository receiptRepo) {
		this.receiptRepo = receiptRepo;
	}

	public Receipt createReceipt(Receipt receipt, User currentUser) {
		receipt.setUser(currentUser);		 	
		return receiptRepo.save(receipt);	
	}

	public List<Receipt> getRecentReceipts(User user, Integer resultSize) {
		
		PageRequest page = PageRequest.of(0, resultSize, Sort.by("date").descending());		
		return receiptRepo.findAllByUser(user, page);
	}
	
	public Optional<Receipt> getById(User user, Long id) {
		
		Optional<Receipt> result = receiptRepo.findById(id);		
		if (result.isPresent() && currentUserOwnsReceipt(user, result))	return result;
		return Optional.empty();
	}

	public void deleteReceipt(User user, Long id) {
		
		Optional<Receipt> deletingReceipt = receiptRepo.findById(id);
		
		if(deletingReceipt.isPresent() && currentUserOwnsReceipt(user, deletingReceipt)) {
			receiptRepo.deleteById(id);
		}
		// deleting transfer with rec
	}
	
	private boolean currentUserOwnsReceipt(User user, Optional<Receipt> receipt) {
		if(receipt.get().getUser().getId() == user.getId()) return true;
		return false;
	}
	
	public List<Receipt> searchReceiptsByClientName(User user, String key) {
		
		List<Receipt> receiptsBase = receiptRepo.findAllByClientContainingIgnoreCase(key);	
		return receiptsBase.stream().filter(rec -> rec.getUser().getId().equals(user.getId())).toList();
	}
	
	
}



















