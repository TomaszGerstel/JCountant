package com.tgerstel.calculations;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tgerstel.calculations.BalanceResults;

class BalanceResultsTest {
	
	static BalanceResults balanceResult;
	
	@BeforeAll
	static void prepareVariable() {
		balanceResult = new BalanceResults(
				BigDecimal.valueOf(800), BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), BigDecimal.valueOf(1600),
				BigDecimal.valueOf(400), BigDecimal.valueOf(200), BigDecimal.valueOf(100), null);
	}

	@Test
	void testGetCosts() {		
		assertEquals(BigDecimal.valueOf(800) , balanceResult.getCosts());
	}
	
	@Test
	void testGetGrossCosts() {		
		assertEquals(BigDecimal.valueOf(1000) , balanceResult.getGrossCosts());
	}
	
	@Test
	void testGetGrossIncome() {		
		assertEquals(BigDecimal.valueOf(2000) , balanceResult.getGrossIncome());
	}
	
	@Test
	void testGetNetIncome() {		
		assertEquals(BigDecimal.valueOf(1600) , balanceResult.getNetIncome());
	}
	
	
	@Test
	void testGetProfitPaid() {		
		assertEquals(BigDecimal.valueOf(400) , balanceResult.getProfitPaid());
	}
	
	@Test
	void testGetVatPaid() {		
		assertEquals(BigDecimal.valueOf(200) , balanceResult.getVatPaid());
	}
	
	@Test
	void testGetTaxPaid() {		
		assertEquals(BigDecimal.valueOf(100) , balanceResult.getTaxPaid());
	}

	
	@Test
	void testGetNetBalance() {		
		assertEquals(BigDecimal.valueOf(800) , balanceResult.getNetBalance());
	}
	
	@Test
	void testGetLumpTaxRate() {		
		assertEquals(12, balanceResult.getLumpTaxRate());
	}
	
	@Test
	void testGetLumpSumTaxDue() {		
		assertEquals(BigDecimal.valueOf(192) , balanceResult.getLumpSumTaxDue());
	}
	
	@Test
	void testGetFlatTaxDue() {		
		assertEquals(BigDecimal.valueOf(152) , balanceResult.getFlatTaxDue());
	}
	
	@Test
	void testGetVatBalance() {		
		assertEquals(BigDecimal.valueOf(0) , balanceResult.getVatBalance());
	}
	
	// getflatTaxBalance
	
}
