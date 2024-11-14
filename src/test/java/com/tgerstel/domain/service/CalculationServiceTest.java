package com.tgerstel.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tgerstel.domain.BalanceResults;
import com.tgerstel.domain.Transaction;
import com.tgerstel.domain.repository.TransferRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tgerstel.infrastructure.repository.Receipt;
import com.tgerstel.infrastructure.repository.Transfer;
import com.tgerstel.domain.TransferType;
import com.tgerstel.infrastructure.repository.User;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

	@Mock
	private TransferRepository transferRepo;
	@InjectMocks
	private BalanceCalculationService calcService;

	static List<Transaction> transactions;
	static List<Transfer> transfers;
	static Transaction transaction1_In;
	static Transaction transaction2_Out;
	static Transaction transaction3_Salary;
	static Transaction transaction4_Tax;
	static Transaction transaction5_Vat;
	static Transfer transf1_In;
	static Transfer transf2_Out;
	static Transfer transf3_Salary;
	static Transfer transf4_Tax;
	static Transfer transf5_Vat;
	static Receipt receipt1;
	static LocalDate date1_now;
	static LocalDate date2_month_ago;
	static BalanceResults balanceResultsForKnowingTransfers;
	static User user;

	@BeforeAll
	static void prepareVariables() {

		date1_now = LocalDate.now();
		date2_month_ago = date1_now.minusMonths(1);
		transfers = new ArrayList<Transfer>();
		transaction1_In = new Transaction(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200).setScale(2),
				BigDecimal.valueOf(200).setScale(2), BigDecimal.valueOf(1000).setScale(2));
		transaction2_Out = new Transaction(TransferType.OUT_TRANSFER, BigDecimal.valueOf(200).setScale(2),
				BigDecimal.valueOf(20).setScale(2), BigDecimal.valueOf(180).setScale(2));
		transaction3_Salary = new Transaction(TransferType.SALARY, BigDecimal.valueOf(600).setScale(2), null, null);
		transaction4_Tax = new Transaction(TransferType.TAX_OUT_TRANSFER, BigDecimal.valueOf(100).setScale(2), null, null);
		transaction5_Vat = new Transaction(TransferType.VAT_OUT_TRANSFER, BigDecimal.valueOf(100).setScale(2), null, null);

		transactions = List.of(transaction1_In, transaction2_Out, transaction3_Salary, transaction4_Tax,
				transaction5_Vat);

		transf1_In = new Transfer(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), null, null, date1_now, null,
				new Receipt(date1_now, BigDecimal.valueOf(1200), BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null, "client", "worker", "desc", null), new User());
		transf2_Out = new Transfer(TransferType.OUT_TRANSFER, BigDecimal.valueOf(200), null, null, date2_month_ago, null,
				new Receipt(date2_month_ago, BigDecimal.valueOf(200), BigDecimal.valueOf(180), BigDecimal.valueOf(20), null, "client", "worker", "desc", null), new User());
		transf3_Salary = new Transfer(TransferType.SALARY, BigDecimal.valueOf(600), null, null, date2_month_ago, null, null, new User());
		transf4_Tax = new Transfer(TransferType.TAX_OUT_TRANSFER,BigDecimal.valueOf(100), null, null, date1_now, null, null, new User());
		transf5_Vat = new Transfer(TransferType.VAT_OUT_TRANSFER, BigDecimal.valueOf(100), null, null, date2_month_ago, null, null,
				new User());

		transfers = List.of(transf1_In, transf2_Out, transf3_Salary, transf4_Tax, transf5_Vat);

		// add variables to show calculations and take values from transfers
		balanceResultsForKnowingTransfers = new BalanceResults(BigDecimal.valueOf(180).setScale(2), BigDecimal.valueOf(200).setScale(2), BigDecimal.valueOf(1200).setScale(2),
				BigDecimal.valueOf(1000).setScale(2), BigDecimal.valueOf(600).setScale(2),BigDecimal.valueOf(100).setScale(2), 
				BigDecimal.valueOf(100).setScale(2), null);

		user = new User();
		user.setLumpSumTaxRate(12);
		receipt1 = new Receipt(date1_now, BigDecimal.valueOf(650), BigDecimal.valueOf(600), BigDecimal.valueOf(50), null, "WallMark", "Stan", "for service", null);

	}

	@Test
	@DisplayName("CurrentBalanceMethod should return right balance calculations to known transfers")
	void testCurrentBalance() {

		Mockito.when(transferRepo.getAllForUser(ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.currentBalance(new User());

		assertAll(() -> assertEquals(BigDecimal.valueOf(200).setScale(2), result.getBalance()),
				() -> assertEquals(BigDecimal.valueOf(180).setScale(2), result.getCosts()),
				() -> assertEquals(BigDecimal.valueOf(55.8).setScale(2), result.getFlatTaxBalance().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(200).setScale(2), result.getGrossCosts()),
				() -> assertEquals(BigDecimal.valueOf(1200).setScale(2), result.getGrossIncome()),
				() -> assertEquals(BigDecimal.valueOf(155.8).setScale(2), result.getFlatTaxDue().setScale(2)),
				() -> assertEquals(19, result.getFlatTaxRate()),
				() -> assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getNetIncome()),
				() -> assertEquals(BigDecimal.valueOf(20).setScale(2), result.getLumpSumTaxBalance().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(120).setScale(2), result.getLumpSumTaxDue().setScale(2)),
				() -> assertEquals(12, result.getLumpTaxRate()),
				() -> assertEquals(BigDecimal.valueOf(820).setScale(2), result.getNetBalance()),
				() -> assertEquals(BigDecimal.valueOf(800).setScale(2), result.getOtherCosts()),
				() -> assertEquals(BigDecimal.valueOf(664.2).setScale(2), result.getProfitDueFlat()),
				() -> assertEquals(BigDecimal.valueOf(700).setScale(2), result.getProfitDueLump().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(600).setScale(2), result.getProfitPaid()),
				() -> assertEquals(BigDecimal.valueOf(64.2).setScale(2), result.getProfitRemainingFlat().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(100).setScale(2), result.getProfitRemainingLump().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(100).setScale(2), result.getTaxPaid()),
				() -> assertEquals(BigDecimal.valueOf(80).setScale(2), result.getVatBalance().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(180).setScale(2), result.getVatDue().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(100).setScale(2), result.getVatPaid()));
	}

	@Test
	@DisplayName("CurrentBalanceMethod should return right balance calculations to known transfers")
	void testBalanceToCurrentMonth() {

		Mockito.when(transferRepo.getForUserInRange(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.balanceToCurrentMonth(new User());
		// change to equal prepared balance result?
		assertAll(() -> assertEquals(BigDecimal.valueOf(200).setScale(2), result.getBalance()),
				() -> assertEquals(BigDecimal.valueOf(180).setScale(2), result.getCosts()),
				() -> assertEquals(BigDecimal.valueOf(55.8).setScale(2), result.getFlatTaxBalance().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(200).setScale(2), result.getGrossCosts()),
				() -> assertEquals(BigDecimal.valueOf(1200).setScale(2), result.getGrossIncome()),
				() -> assertEquals(BigDecimal.valueOf(155.8).setScale(2), result.getFlatTaxDue().setScale(2)),
				() -> assertEquals(19, result.getFlatTaxRate()),
				() -> assertEquals(BigDecimal.valueOf(1000).setScale(2), result.getNetIncome()),
				() -> assertEquals(BigDecimal.valueOf(20).setScale(2), result.getLumpSumTaxBalance().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(120).setScale(2), result.getLumpSumTaxDue().setScale(2)),
				() -> assertEquals(12, result.getLumpTaxRate()),
				() -> assertEquals(BigDecimal.valueOf(820).setScale(2), result.getNetBalance()),
				() -> assertEquals(BigDecimal.valueOf(800).setScale(2), result.getOtherCosts()),
				() -> assertEquals(BigDecimal.valueOf(664.2).setScale(2), result.getProfitDueFlat()),
				() -> assertEquals(BigDecimal.valueOf(700).setScale(2), result.getProfitDueLump().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(600).setScale(2), result.getProfitPaid()),
				() -> assertEquals(BigDecimal.valueOf(64.2).setScale(2), result.getProfitRemainingFlat().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(100).setScale(2), result.getProfitRemainingLump().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(100).setScale(2), result.getTaxPaid()),
				() -> assertEquals(BigDecimal.valueOf(80).setScale(2), result.getVatBalance().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(180).setScale(2), result.getVatDue().setScale(2)),
				() -> assertEquals(BigDecimal.valueOf(100).setScale(2), result.getVatPaid()));
	}

	@Test
	@DisplayName("LastMonthBalanceMethod should return right balance calculations to known balance result")
	void testBalanceToLastMonth() {

		Mockito.when(transferRepo.getForUserInRange(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.balanceToLastMonth(new User());
		assertEquals(balanceResultsForKnowingTransfers, result);
	}

	@Test
	@DisplayName("BalanceToDataRange Method should return right balance calculations to known balance result")
	void testBalanceToDateRange() {

		Mockito.when(transferRepo.getForUserInRange(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.balanceToDateRange(date1_now, date2_month_ago, new User());
		assertEquals(balanceResultsForKnowingTransfers, result);
	}

	@Test
	@DisplayName("CalculateBalance method should return right balance calculations to known balance result")
	void testCalculateBalance() {
		// remove and change method to private?
		BalanceResults result = calcService.calculateBalance(transfers, new User());
		assertEquals(balanceResultsForKnowingTransfers, result);
	}

	@Test
	@DisplayName("CalculateTransactionObject method should return right transaction lisf from known transfer list")
	void testCreateTransactionObject() {

		List<Transaction> result = calcService.createTransactionObjects(transfers);
		assertEquals(transactions, result);
	}

	@Test
	@DisplayName("MakeTransactionFromReceipt method should return right transaction object from known receipt")
	void testMakeTransactionFromReceipt() {

		Transaction result = calcService.makeTransactionFromReceipt(receipt1);

		assertAll(() -> assertEquals(BigDecimal.valueOf(650).setScale(2), result.getAmount()),
				() -> assertEquals(BigDecimal.valueOf(600).setScale(2), result.getNetAmount()),
				() -> assertEquals(BigDecimal.valueOf(50).setScale(2), result.getVatValue()));
	}

	@Test
	@DisplayName("TransferTypeWithoutReceipt method should return true to transfer type without receipt")
	void testTransferTypeWithoutReceipt() {

		assertAll(() -> assertTrue(calcService.transferTypeWithoutReceipt(transf3_Salary)),
				() -> assertTrue(calcService.transferTypeWithoutReceipt(transf4_Tax)),
				() -> assertTrue(calcService.transferTypeWithoutReceipt(transf5_Vat)));		
	}
	
	@Test
	@DisplayName("TransferTypeWithoutReceipt method should return false to transfer type with receipt")
	void testTransferTypeWithoutReceipt2() {

		assertAll(() -> assertFalse(calcService.transferTypeWithoutReceipt(transf1_In)),
				() -> assertFalse(calcService.transferTypeWithoutReceipt(transf2_Out)));		
	}

	@Test
	void testCalculateCosts() {			
		assertEquals(BigDecimal.valueOf(180).setScale(2), calcService.calculateCosts(transactions));
	}	
	
	@Test
	void testCalculateGrossIncome() {
		assertEquals(BigDecimal.valueOf(1200).setScale(2), calcService.calculateGrossIncome(transactions));
	}

	@Test
	void testCalculateProfitPaid() {
		assertEquals(BigDecimal.valueOf(600).setScale(2), calcService.calculateProfitPaid(transactions));
	}

	@Test
	void testCalculateVatPaid() {
		assertEquals(BigDecimal.valueOf(100).setScale(2), calcService.calculateVatPaid(transactions));
	}

	@Test
	void testCalculateTaxPaid() {
		assertEquals(BigDecimal.valueOf(100).setScale(2), calcService.calculateTaxPaid(transactions));
	}

}
