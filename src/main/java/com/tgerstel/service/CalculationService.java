package com.tgerstel.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tgerstel.model.BalanceResults;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.TransactionModel;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;
import com.tgerstel.repository.TransferRepository;

@Service
public class CalculationService {
	
	private ReceiptRepository receiptRepo;
	private TransferRepository transferRepo;
	
	public CalculationService(ReceiptRepository receiptRepo, TransferRepository transferRepo) {
		super();
		this.receiptRepo = receiptRepo;
		this.transferRepo = transferRepo;
	}
	
	
	private BalanceResults calculateBalance (List<Transfer> transfers, User user) {
		List<TransactionModel> transactions = createTransactionObjects(transfers);
		BalanceResults balanceResults = new BalanceResults();
		
		balanceResults.setCosts(calculateCosts(transactions));
		balanceResults.setGrossIncome(calculateGrossIncome(transactions));
		
		return balanceResults;
	}

	private List<TransactionModel> createTransactionObjects(List<Transfer> transfers) {
		List<TransactionModel> transactions = new ArrayList<>();
		TransactionModel transaction;
		
		for(Transfer t : transfers) {
			//metoda
			transaction = new TransactionModel();
			if(t.getReceipt() != null) {
				transaction.setTransferType(t.getTransferType());			
				transaction.setAmount(t.getReceipt().getAmount());
				transaction.setNetAmount(t.getReceipt().getNetAmount());
				transaction.setVatValue(t.getReceipt().getVatValue());
				transaction.setVatPercentage(t.getReceipt().getVatPercentage());
				
				transactions.add(transaction);
			}
			else
				if(transferTypeWithoutReceipt(t)) {
					transaction.setAmount(t.getAmount());
					transaction.setTransferType(t.getTransferType());
					
					transactions.add(transaction);
				}			
		}
		return transactions;
	}
	
	private boolean transferTypeWithoutReceipt(Transfer transfer) {
		TransferType t = transfer.getTransferType();
		return (t == TransferType.SALARY || t == TransferType.TAX_OUT_TRANSFER
				|| t == TransferType.VAT_OUT_TRANSFER);
	}
	
	private Float calculateCosts(List<TransactionModel> transactions) {
		float sum = 0;		
		for(TransactionModel t : transactions) {
			if(t.getTransferType() == TransferType.OUT_TRANSFER) {
				float vat = 0;
				if(t.getVatValue() != null) vat = t.getVatValue();
				else
					if(t.getVatPercentage() != null) vat = t.getAmount() / (t.getVatPercentage() / 100);
				sum += t.getAmount() - vat;				
			}
		}		
		return sum;
	}
	
	private Float calculateGrossIncome(List<TransactionModel> transactions) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
