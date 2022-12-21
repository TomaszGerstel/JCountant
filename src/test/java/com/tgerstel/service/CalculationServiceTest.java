package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tgerstel.model.BalanceResults;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.TransactionModel;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.TransferRepository;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {
	
	@Mock private TransferRepository transferRepo;	
	@InjectMocks private CalculationService calcService;
	
	static List<TransactionModel> transactions;
	static List<Transfer> transfers;
	static TransactionModel trans1_In;
	static TransactionModel trans2_Out;
	static TransactionModel trans3_Salary;
	static TransactionModel trans4_Tax;
	static TransactionModel trans5_Vat;
	static Transfer transf1_In;
	static Transfer transf2_Out;
	static Transfer transf3_Salary;
	static Transfer transf4_Tax;
	static Transfer transf5_Vat;
	static Receipt receipt1;
	static LocalDate date1_now;
	static LocalDate date2_month_ago;
	
	@BeforeAll
	static void prepareVariables() {		

		date1_now = LocalDate.now();
		date2_month_ago = date1_now.minusMonths(1);
		transfers = new ArrayList<Transfer>();
		trans1_In = new TransactionModel(TransferType.IN_TRANSFER, 1200f, 200f, 1000f);
		trans2_Out = new TransactionModel(TransferType.OUT_TRANSFER, 200f, 20f, 180f);
		trans3_Salary = new TransactionModel(TransferType.SALARY, 600f, null, null);
		trans4_Tax = new TransactionModel(TransferType.TAX_OUT_TRANSFER, 150f, null, null);
		trans5_Vat = new TransactionModel(TransferType.VAT_OUT_TRANSFER, 100f, null, null);
		
		transactions = List.of(trans1_In, trans2_Out, trans3_Salary, trans4_Tax, trans5_Vat);
		
		transf1_In = new Transfer(TransferType.IN_TRANSFER, 1200f, null, null, date1_now, null, 
				new Receipt(date1_now, 1200f, 1000f, 200f, null, "client", "worker", "desc", null),	new User());
		transf2_Out = new Transfer(TransferType.OUT_TRANSFER, 200f, null, null, date2_month_ago, null, 
				new Receipt(date2_month_ago, 200f, 180f, 20f, null, "client", "worker", "desc", null), new User());
		transf3_Salary = new Transfer(TransferType.SALARY, 600f, null, null, date2_month_ago, null, null, new User());
		transf4_Tax = new Transfer(TransferType.TAX_OUT_TRANSFER, 100f, null, null, date1_now, null, null, new User());
		transf5_Vat = new Transfer(TransferType.VAT_OUT_TRANSFER, 100f, null, null, date2_month_ago, null, null, new User());
		
		transfers = List.of(transf1_In, transf2_Out, transf3_Salary, transf4_Tax, transf5_Vat);
		
	}

	@Test
	@DisplayName("CurrentBalanceMethod should return right balance calculations to known transfers")
	void testCurrentBalance() {
		
Mockito.when(transferRepo.findAllByUser(ArgumentMatchers.any())).thenReturn(transfers);
		
		BalanceResults result = calcService.currentBalance(new User());
		
		assertAll(
				() -> assertEquals(200, result.getBalance()), // 1100
				() -> assertEquals(180, result.getCosts()), // 0
				() -> assertEquals(55.8f,(result.getFlatTaxBalance()*10)/10), //90
				() -> assertEquals(155.8f, result.getFlatTaxDue()), //190
				() -> assertEquals(19, result.getFlatTaxRate()), //19
				() -> assertEquals(1000, result.getGrossIncome()), //1000
				() -> assertEquals(20, result.getLumpSumTaxBalance()), //20
				() -> assertEquals(120, result.getLumpSumTaxDue()), //120
				() -> assertEquals(12, result.getLumpTaxRate()), //12
				() -> assertEquals(820, result.getNetBalance()), //1000
				() -> assertEquals(800, result.getOtherCosts()), //100
				() -> assertEquals(664.2f, result.getProfitDueFlat()), //810
				() -> assertEquals(700, result.getProfitDueLump()), //880
				() -> assertEquals(600, result.getProfitPaid()), // 0
				() -> assertEquals(64, Math.round(result.getProfitRemainingFlat())), //810
				() -> assertEquals(100, result.getProfitRemainingLump()), //880
				() -> assertEquals(100, result.getTaxPaid()), //100
				() -> assertEquals(80, result.getVatBalance()), //200
				() -> assertEquals(180, result.getVatDue()), //200
				() -> assertEquals(100, result.getVatPaid())	//0
				);
	}

	@Test
	@DisplayName("CurrentBalanceMethod should return right balance calculations to known transfers"
			+ " with current month date")
	void testBalanceToCurrentMonth() {

		
		Mockito.when(transferRepo.findAllByDateAfterAndDateBeforeAndUser(
				ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(transfers);
		
		BalanceResults result = calcService.balanceToCurrentMonth(new User());
		
		assertAll(
				() -> assertEquals(200, result.getBalance()),
				() -> assertEquals(180, result.getCosts()),
				() -> assertEquals(55.8f,(result.getFlatTaxBalance()*10)/10),
				() -> assertEquals(155.8f, result.getFlatTaxDue()),
				() -> assertEquals(19, result.getFlatTaxRate()),
				() -> assertEquals(1000, result.getGrossIncome()),
				() -> assertEquals(20, result.getLumpSumTaxBalance()),
				() -> assertEquals(120, result.getLumpSumTaxDue()),
				() -> assertEquals(12, result.getLumpTaxRate()),
				() -> assertEquals(820, result.getNetBalance()),
				() -> assertEquals(800, result.getOtherCosts()),
				() -> assertEquals(664.2f, result.getProfitDueFlat()),
				() -> assertEquals(700, result.getProfitDueLump()),
				() -> assertEquals(600, result.getProfitPaid()),
				() -> assertEquals(64, Math.round(result.getProfitRemainingFlat())),
				() -> assertEquals(100, result.getProfitRemainingLump()),
				() -> assertEquals(100, result.getTaxPaid()),
				() -> assertEquals(80, result.getVatBalance()), 
				() -> assertEquals(180, result.getVatDue()),
				() -> assertEquals(100, result.getVatPaid())	
				);
	}
//
//	@Test
//	void testBalanceToLastMonth() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testBalanceToDateRange() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateBalance() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCreateTransactionObjects() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testMakeTransactionFromReceipt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testTransferTypeWithoutReceipt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateCosts() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateGrossIncome() {
//		assertEquals(1000, calcService.calculateGrossIncome(transactions));
//	}
//
//	@Test
//	void testCalculateProfitPaid() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateNetBalance() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateVatDue() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateVatPaid() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCalculateTaxPaid() {
//		fail("Not yet implemented");
//	}

}
