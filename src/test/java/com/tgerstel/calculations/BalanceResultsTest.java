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
				BigDecimal.valueOf(1000),BigDecimal.valueOf(5000), BigDecimal.valueOf(4000), BigDecimal.valueOf(1200),
				BigDecimal.valueOf(100), BigDecimal.valueOf(200), BigDecimal.valueOf(150), null);
	}

	@Test
	void testGetCosts() {		
		assertEquals(BigDecimal.valueOf(1000) , balanceResult.getCosts());
	}
	
	@Test
	void testGetNetIncome() {		
		assertEquals(BigDecimal.valueOf(5000) , balanceResult.getNetIncome());
	}

}
