package com.tgerstel.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BalanceResultsTest {
	
	static BalanceResults balanceResult;
	
	@BeforeAll
	static void prepareVariable() {
		balanceResult = new BalanceResults(
				BigDecimal.valueOf(800).setScale(2), BigDecimal.valueOf(1000).setScale(2),
				BigDecimal.valueOf(2000).setScale(2), BigDecimal.valueOf(1600).setScale(2),
				BigDecimal.valueOf(400).setScale(2), BigDecimal.valueOf(200).setScale(2),
				BigDecimal.valueOf(100).setScale(2), null);
	}

	@Test
	void testGetCosts() {		
		assertEquals(BigDecimal.valueOf(800).setScale(2) , balanceResult.getCosts());
	}
	
	@Test
	void testGetGrossCosts() {		
		assertEquals(BigDecimal.valueOf(1000).setScale(2) , balanceResult.getGrossCosts());
	}
	
	@Test
	void testGetGrossIncome() {		
		assertEquals(BigDecimal.valueOf(2000).setScale(2) , balanceResult.getGrossIncome());
	}
	
	@Test
	void testGetNetIncome() {		
		assertEquals(BigDecimal.valueOf(1600).setScale(2) , balanceResult.getNetIncome());
	}	
	
	@Test
	void testGetProfitPaid() {		
		assertEquals(BigDecimal.valueOf(400).setScale(2) , balanceResult.getProfitPaid());
	}
	
	@Test
	void testGetVatPaid() {		
		assertEquals(BigDecimal.valueOf(200).setScale(2) , balanceResult.getVatPaid());
	}
	
	@Test
	void testGetTaxPaid() {		
		assertEquals(BigDecimal.valueOf(100).setScale(2) , balanceResult.getTaxPaid());
	}
	
	@Test
	void testGetNetBalance() {		
		assertEquals(BigDecimal.valueOf(800).setScale(2) , balanceResult.getNetBalance());
	}
	
	@Test
	void testGetLumpTaxRate() {		
		assertEquals(12, balanceResult.getLumpTaxRate());
	}
	
	@Test
	void testGetLumpSumTaxDue() {		
		assertEquals(BigDecimal.valueOf(192).setScale(2) , balanceResult.getLumpSumTaxDue());
	}
	
	@Test
	void testGetFlatTaxDue() {		
		assertEquals(BigDecimal.valueOf(152).setScale(2) , balanceResult.getFlatTaxDue());
	}
	
	@Test
	void testGetVatBalance() {		
		assertEquals(BigDecimal.valueOf(0).setScale(2) , balanceResult.getVatBalance());
	}
		
	@Test
	void testGetFlatTaxBalance() {		
		assertEquals(BigDecimal.valueOf(52).setScale(2) , balanceResult.getFlatTaxBalance());
	}

	@Test
	void testGetLumpSumTaxBalance() {		
		assertEquals(BigDecimal.valueOf(92).setScale(2) , balanceResult.getLumpSumTaxBalance());
	}
	
	@Test
	void testGetProfitDueFlat() {		
		assertEquals(BigDecimal.valueOf(648).setScale(2) , balanceResult.getProfitDueFlat());
	}
	
	@Test
	void testGetProfitRemainingFlat() {		
		assertEquals(BigDecimal.valueOf(248).setScale(2) , balanceResult.getProfitRemainingFlat());
	}
	
	@Test
	void testGetProfitDueLump() {		
		assertEquals(BigDecimal.valueOf(608).setScale(2) , balanceResult.getProfitDueLump());
	}
	
	@Test
	void testGetProfitRemainingLump() {		
		assertEquals(BigDecimal.valueOf(208).setScale(2) , balanceResult.getProfitRemainingLump());
	}
	
	@Test
	void testGetFlatTaxRate() {		
		assertEquals(19f , balanceResult.getFlatTaxRate());
	}
	
	@Test
	void testGetOtherCosts() {		
		assertEquals(BigDecimal.valueOf(700).setScale(2) , balanceResult.getOtherCosts());
	}
	
	@Test
	void testGetBalance() {		
		assertEquals(BigDecimal.valueOf(300).setScale(2) , balanceResult.getBalance());
	}
	
	@Test
	void testGetVatDue() {		
		assertEquals(BigDecimal.valueOf(200).setScale(2) , balanceResult.getVatDue());
	}
}
