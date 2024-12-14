package com.tgerstel.infrastructure.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tgerstel.domain.Receipt;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReceiptTest {

	static Receipt receiptWithAllValues;
	static Receipt receiptWithOnlyVatValue;
	static Receipt receiptWithOnlyNetAmount;
	static Receipt receiptWithOnlyVatPercentage;
	static Receipt receiptWthoutOptValues;
	static LocalDate date;

	@BeforeAll
	static void prepareVariables() {
		date = LocalDate.now();
		receiptWithAllValues = new Receipt(1L, date, BigDecimal.valueOf(3600), BigDecimal.valueOf(3000),
				BigDecimal.valueOf(600), 20f, "client1", "worker1", "desc", null);
		receiptWithOnlyVatValue = new Receipt(2L, date, BigDecimal.valueOf(3000), null, BigDecimal.valueOf(500), null,
				"client2", "worker1", "desc", null);
		receiptWithOnlyNetAmount = new Receipt(3L, date, BigDecimal.valueOf(3000), BigDecimal.valueOf(2500), null, null,
				"client3", "worker2", "desc", null);
		receiptWithOnlyVatPercentage = new Receipt(4L, date, BigDecimal.valueOf(3750), null, null, 25f, "client1",
				"worker2", "desc", null);
		receiptWthoutOptValues = new Receipt(5L, date, BigDecimal.valueOf(3000), null, null, null, "client4", "worker1",
				"desc", null);
	}

	@Test
	@DisplayName("getNetAmoutn for object with declared all fields should return known value")
	void testGetNetAmount() {
		assertEquals(BigDecimal.valueOf(3000).setScale(2), receiptWithAllValues.getNetAmount());
	}
	
	@Test
	@DisplayName("getNetAmount should return known (calculated from vatValue) value (amount - vatValue)")
	void testGetNetAmount2() {
		assertEquals(BigDecimal.valueOf(2500).setScale(2), receiptWithOnlyVatValue.getNetAmount());
	}
	
	@Test
	@DisplayName("getNetAmoutn for object with declared netAmount should return known value")
	void testGetNetAmount3() {
		assertEquals(BigDecimal.valueOf(2500).setScale(2), receiptWithOnlyNetAmount.getNetAmount());
	}
	
	@Test
	@DisplayName("getNetAmount should return known (calculated from vatPercentage) value")
	void testGetNetAmount4() {
		assertEquals(BigDecimal.valueOf(3000).setScale(2), receiptWithOnlyVatPercentage.getNetAmount());
	}
	
	@Test
	@DisplayName("getNetValue for object without vatValue, netValue and vatPercentage shoud return null")
	void testGetNetAmount5() {
		assertNull(receiptWthoutOptValues.getNetAmount());
	}	
	
	@Test
	@DisplayName("getVatValue for object with declared all fields should return known value")
	void testGetVataValue() {
		assertEquals(BigDecimal.valueOf(600).setScale(2), receiptWithAllValues.getVatValue());
	}
	
	@Test
	@DisplayName("getVatValue for object with declared catCalue should return known value")
	void testGetVatValue2() {
		assertEquals(BigDecimal.valueOf(500).setScale(2), receiptWithOnlyVatValue.getVatValue());
	}
	
	@Test
	@DisplayName("getVatValue should return known (calculated from netAmount) value")
	void testGetVatValue3() {
		assertEquals(BigDecimal.valueOf(500).setScale(2), receiptWithOnlyNetAmount.getVatValue());
	}
	
	@Test
	@DisplayName("getVatValue should return known (calculated from vatPercentage) value")
	void testGetVatValue4() {
		assertEquals(BigDecimal.valueOf(750).setScale(2), receiptWithOnlyVatPercentage.getVatValue());
	}
	
	@Test
	@DisplayName("getVatValue for object without vatValue, netValue and vatPercentage shoud return null")
	void testGetVatValue5() {
		assertNull(receiptWthoutOptValues.getVatValue());
	}

}
