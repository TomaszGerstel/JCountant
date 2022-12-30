package com.tgerstel.calculations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.validateMockitoUsage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tgerstel.calculations.BalanceResults;
import com.tgerstel.calculations.CalculationService;
import com.tgerstel.calculations.TransactionModel;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.TransferRepository;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

	@Mock
	private TransferRepository transferRepo;
	@InjectMocks
	private CalculationService calcService;

	static List<TransactionModel> transactions;
	static List<Transfer> transfers;
	static TransactionModel transaction1_In;
	static TransactionModel transaction2_Out;
	static TransactionModel transaction3_Salary;
	static TransactionModel transaction4_Tax;
	static TransactionModel transaction5_Vat;
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
		transaction1_In = new TransactionModel(TransferType.IN_TRANSFER, 1200f, 200f, 1000f);
		transaction2_Out = new TransactionModel(TransferType.OUT_TRANSFER, 200f, 20f, 180f);
		transaction3_Salary = new TransactionModel(TransferType.SALARY, 600f, null, null);
		transaction4_Tax = new TransactionModel(TransferType.TAX_OUT_TRANSFER, 100f, null, null);
		transaction5_Vat = new TransactionModel(TransferType.VAT_OUT_TRANSFER, 100f, null, null);

		transactions = List.of(transaction1_In, transaction2_Out, transaction3_Salary, transaction4_Tax,
				transaction5_Vat);

		transf1_In = new Transfer(TransferType.IN_TRANSFER, 1200f, null, null, date1_now, null,
				new Receipt(date1_now, 1200f, 1000f, 200f, null, "client", "worker", "desc", null), new User());
		transf2_Out = new Transfer(TransferType.OUT_TRANSFER, 200f, null, null, date2_month_ago, null,
				new Receipt(date2_month_ago, 200f, 180f, 20f, null, "client", "worker", "desc", null), new User());
		transf3_Salary = new Transfer(TransferType.SALARY, 600f, null, null, date2_month_ago, null, null, new User());
		transf4_Tax = new Transfer(TransferType.TAX_OUT_TRANSFER, 100f, null, null, date1_now, null, null, new User());
		transf5_Vat = new Transfer(TransferType.VAT_OUT_TRANSFER, 100f, null, null, date2_month_ago, null, null,
				new User());

		transfers = List.of(transf1_In, transf2_Out, transf3_Salary, transf4_Tax, transf5_Vat);

		// add variables to show calculations and take values from transfers
		balanceResultsForKnowingTransfers = new BalanceResults(180f, 1000f, 820f, 180f, 600f, 100f, 100f, null);

		user = new User();
		user.setLumpSumTaxRate(12);
		receipt1 = new Receipt(date1_now, 650f, 600f, 50f, null, "WallMark", "Stan", "for service", null);

	}

	@Test
	@DisplayName("CurrentBalanceMethod should return right balance calculations to known transfers")
	void testCurrentBalance() {

		Mockito.when(transferRepo.findAllByUser(ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.currentBalance(new User());

		assertAll(() -> assertEquals(200, result.getBalance()), () -> assertEquals(180, result.getCosts()),
				() -> assertEquals(55.8f, (result.getFlatTaxBalance() * 10) / 10),
				() -> assertEquals(155.8f, result.getFlatTaxDue()), () -> assertEquals(19, result.getFlatTaxRate()),
				() -> assertEquals(1000, result.getGrossIncome()),
				() -> assertEquals(20, result.getLumpSumTaxBalance()),
				() -> assertEquals(120, result.getLumpSumTaxDue()), () -> assertEquals(12, result.getLumpTaxRate()),
				() -> assertEquals(820, result.getNetBalance()), () -> assertEquals(800, result.getOtherCosts()),
				() -> assertEquals(664.2f, result.getProfitDueFlat()),
				() -> assertEquals(700, result.getProfitDueLump()), () -> assertEquals(600, result.getProfitPaid()),
				() -> assertEquals(64, Math.round(result.getProfitRemainingFlat())),
				() -> assertEquals(100, result.getProfitRemainingLump()), () -> assertEquals(100, result.getTaxPaid()),
				() -> assertEquals(80, result.getVatBalance()), () -> assertEquals(180, result.getVatDue()),
				() -> assertEquals(100, result.getVatPaid()));
	}

	@Test
	@DisplayName("CurrentBalanceMethod should return right balance calculations to known transfers")
	void testBalanceToCurrentMonth() {

		Mockito.when(transferRepo.findAllByDateAfterAndDateBeforeAndUser(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.balanceToCurrentMonth(new User());
		// change to equal prepared balance result?
		assertAll(() -> assertEquals(200, result.getBalance()), () -> assertEquals(180, result.getCosts()),
				() -> assertEquals(55.8f, (result.getFlatTaxBalance() * 10) / 10),
				() -> assertEquals(155.8f, result.getFlatTaxDue()), () -> assertEquals(19, result.getFlatTaxRate()),
				() -> assertEquals(1000, result.getGrossIncome()),
				() -> assertEquals(20, result.getLumpSumTaxBalance()),
				() -> assertEquals(120, result.getLumpSumTaxDue()), () -> assertEquals(12, result.getLumpTaxRate()),
				() -> assertEquals(820, result.getNetBalance()), () -> assertEquals(800, result.getOtherCosts()),
				() -> assertEquals(664.2f, result.getProfitDueFlat()),
				() -> assertEquals(700, result.getProfitDueLump()), () -> assertEquals(600, result.getProfitPaid()),
				() -> assertEquals(64, Math.round(result.getProfitRemainingFlat())),
				() -> assertEquals(100, result.getProfitRemainingLump()), () -> assertEquals(100, result.getTaxPaid()),
				() -> assertEquals(80, result.getVatBalance()), () -> assertEquals(180, result.getVatDue()),
				() -> assertEquals(100, result.getVatPaid()));
	}

	@Test
	@DisplayName("LastMonthBalanceMethod should return right balance calculations to known balance result")
	void testBalanceToLastMonth() {

		Mockito.when(transferRepo.findAllByDateAfterAndDateBeforeAndUser(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.balanceToLastMonth(new User());
		assertEquals(balanceResultsForKnowingTransfers, result);
	}

	@Test
	@DisplayName("BalanceToDataRange Method should return right balance calculations to known balance result")
	void testBalanceToDateRange() {

		Mockito.when(transferRepo.findAllByDateAfterAndDateBeforeAndUser(ArgumentMatchers.any(), ArgumentMatchers.any(),
				ArgumentMatchers.any())).thenReturn(transfers);

		BalanceResults result = calcService.balanceToDateRange(date1_now, date2_month_ago, new User());
		assertEquals(balanceResultsForKnowingTransfers, result);
	}

	@Test
	@DisplayName("CalculateBalance method should return right balance calculations to known balance result")
	void testCalculateBalance() {

		BalanceResults result = calcService.calculateBalance(transfers, new User());
		assertEquals(balanceResultsForKnowingTransfers, result);
	}

	@Test
	@DisplayName("CalculateTransactionObject method should return right transaction lisf from known transfer list")
	void testCreateTransactionObject() {

		List<TransactionModel> result = calcService.createTransactionObjects(transfers);
		assertEquals(transactions, result);
	}

	@Test
	@DisplayName("MakeTransactionFromReceipt method should return right transaction object from known receipt")
	void testMakeTransactionFromReceipt() {

		TransactionModel result = calcService.makeTransactionFromReceipt(receipt1);

		assertAll(() -> assertEquals(650, result.getAmount()),
				() -> assertEquals(600, result.getNetAmount()),
				() -> assertEquals(50, result.getVatValue()));
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
		assertEquals(180, calcService.calculateCosts(transactions));
	}	
	
	@Test
	void testCalculateGrossIncome() {
		assertEquals(1000, calcService.calculateGrossIncome(transactions));
	}

	@Test
	void testCalculateProfitPaid() {
		assertEquals(600, calcService.calculateProfitPaid(transactions));
	}

	@Test
	void testCalculateNetBalance() {
		assertEquals(820, calcService.calculateNetBalance(transactions));
	}

	@Test
	void testCalculateVatDue() {
		assertEquals(180, calcService.calculateVatDue(transactions));
	}

	@Test
	void testCalculateVatPaid() {
		assertEquals(100, calcService.calculateVatPaid(transactions));
	}

	@Test
	void testCalculateTaxPaid() {
		assertEquals(100, calcService.calculateTaxPaid(transactions));
	}

}
