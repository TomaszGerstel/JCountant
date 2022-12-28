package com.tgerstel.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tgerstel.model.BalanceResults;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.TransactionModel;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.TransferRepository;

@Service
public class CalculationService {

	private TransferRepository transferRepo;

	public CalculationService(TransferRepository transferRepo) {
		this.transferRepo = transferRepo;
	}

	public BalanceResults currentBalance(User user) {
		return calculateBalance(transferRepo.findAllByUser(user), user);
	}

	public BalanceResults balanceToCurrentMonth(User user) {
		LocalDate from = YearMonth.now().atDay(1);
		LocalDate to = YearMonth.now().atEndOfMonth();
		return balanceToDateRange(from, to, user);
	}

	public BalanceResults balanceToLastMonth(User user) {
		YearMonth month = YearMonth.from(LocalDate.now().minusMonths(1));
		LocalDate from = month.atDay(1);
        LocalDate to   = month.atEndOfMonth();
		return balanceToDateRange(from, to, user);
	}	
	
	public BalanceResults balanceToDateRange(LocalDate from, LocalDate to, User user) {
		List<Transfer> transfers = transferRepo.findAllByDateAfterAndDateBeforeAndUser(from.minusDays(1), to.plusDays(1), user);
		System.out.println("tra: "+transfers);
		System.out.println("dates: "+from + to);
		return calculateBalance(transfers, user);
	}

	protected BalanceResults calculateBalance(List<Transfer> transfers, User user) {
		List<TransactionModel> transactions = createTransactionObjects(transfers);

		BalanceResults balanceResults = new BalanceResults(calculateCosts(transactions),
				calculateGrossIncome(transactions), calculateNetBalance(transactions), calculateVatDue(transactions),
				calculateProfitPaid(transactions), calculateVatPaid(transactions), calculateTaxPaid(transactions),
				user.getLumpSumTaxRate());
		return balanceResults;
	}

	protected List<TransactionModel> createTransactionObjects(List<Transfer> transfers) {
		List<TransactionModel> transactions = new ArrayList<>();
		TransactionModel transaction;

		for (Transfer t : transfers) {
			transaction = new TransactionModel();			
			if (t.getReceipt() != null) {				
				transaction = makeTransactionFromReceipt(t.getReceipt());
			} else if (transferTypeWithoutReceipt(t)) {
				transaction.setAmount(t.getAmount());				
			}
			transaction.setTransferType(t.getTransferType());
			transactions.add(transaction);
		}
		return transactions;
	}
	
	protected TransactionModel makeTransactionFromReceipt(Receipt receipt) {
		TransactionModel transaction = new TransactionModel();
		transaction.setAmount(receipt.getAmount());
		transaction.setNetAmount(receipt.getNetAmount());
		transaction.setVatValue(receipt.getVatValue());		
		return transaction;
	}

	protected boolean transferTypeWithoutReceipt(Transfer transfer) {
		TransferType t = transfer.getTransferType();
		return (t == TransferType.SALARY || t == TransferType.TAX_OUT_TRANSFER || t == TransferType.VAT_OUT_TRANSFER);
	}

	protected Float calculateCosts(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.OUT_TRANSFER) {
				if (t.getVatValue() != null)
					sum += t.getAmount() - t.getVatValue();
				else
					sum += t.getAmount();
			}
		}
		return sum;
	}

	protected Float calculateGrossIncome(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.IN_TRANSFER) {
				if (t.getVatValue() != null)
					sum += t.getAmount() - t.getVatValue();
				else
					sum += t.getAmount();
			}
		}
		return sum;
	}

	protected Float calculateProfitPaid(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.SALARY)
				sum += t.getAmount();
		}
		return sum;
	}

	protected Float calculateNetBalance(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.IN_TRANSFER)
				sum += t.getNetAmount();
			if (t.getTransferType() == TransferType.OUT_TRANSFER)
				sum -= t.getNetAmount();
		}
		return sum;
	}

	protected Float calculateVatDue(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.IN_TRANSFER)
				sum += t.getVatValue();
			if (t.getTransferType() == TransferType.OUT_TRANSFER)
				sum -= t.getVatValue();
		}
		return sum;
	}

	protected Float calculateVatPaid(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.VAT_OUT_TRANSFER)
				sum += t.getAmount();
		}
		return sum;
	}

	protected Float calculateTaxPaid(List<TransactionModel> transactions) {
		float sum = 0;
		for (TransactionModel t : transactions) {
			if (t.getTransferType() == TransferType.TAX_OUT_TRANSFER)
				sum += t.getAmount();
		}
		return sum;
	}
}
