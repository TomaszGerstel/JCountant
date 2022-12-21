package com.tgerstel.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BalanceResultsTest {
	
	static BalanceResults balanceResult;
	
	@BeforeAll
	static void prepareVariable() {
		balanceResult = new BalanceResults(
				1000f, 5000f, 4000f, 1200f, 100f, 200f, 150f, null);
	}

	@Test
	void testGetCosts() {		
		assertEquals(1000 , balanceResult.getCosts());
	}
	
	@Test
	void testGetGrossIncome() {		
		assertEquals(5000 , balanceResult.getGrossIncome());
	}

}
