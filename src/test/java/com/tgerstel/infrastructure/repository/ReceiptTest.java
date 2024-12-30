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
	static Receipt receiptWithoutOptValues;
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
		receiptWithoutOptValues = new Receipt(5L, date, BigDecimal.valueOf(3000), null, null, null, "client4", "worker1",
				"desc", null);
	}

	@Test
	@DisplayName("should return known net amount value")
	void testGetNetAmount() {
		assertEquals(BigDecimal.valueOf(3000).setScale(2), receiptWithAllValues.getNetAmount());
	}
	
	@Test
	@DisplayName("should return calculated net amount value from vat value")
	void testGetCalculatedNetAmountFromVatValue() {
		assertEquals(BigDecimal.valueOf(2500).setScale(2), receiptWithOnlyVatValue.getNetAmount());
	}
	
	@Test
	@DisplayName("should return calculated net amount from vat percentage value")
	void testGetCalculatedNetAmountFromVatPercentage() {
		assertEquals(BigDecimal.valueOf(3000).setScale(2), receiptWithOnlyVatPercentage.getNetAmount());
	}
	
	@Test
	@DisplayName("should return null net amount for missing values")
	void testGetNetAmountForMissingValues() {
		assertNull(receiptWithoutOptValues.getNetAmount());
	}	
	
	@Test
	@DisplayName("should return known vat value")
	void testGetVatValue() {
		assertEquals(BigDecimal.valueOf(600).setScale(2), receiptWithAllValues.getVatValue());
	}
	
	@Test
	@DisplayName("should return vat value calculated from net amount")
	void testGetVatValueFromNetAmount() {
		assertEquals(BigDecimal.valueOf(500).setScale(2), receiptWithOnlyNetAmount.getVatValue());
	}
	
	@Test
	@DisplayName("should return vat value calculated from vat percentage")
	void testGetVatValueFromVatPercentage() {
		assertEquals(BigDecimal.valueOf(750).setScale(2), receiptWithOnlyVatPercentage.getVatValue());
	}
	
	@Test
	@DisplayName("should return null vat value for missing values")
	void testGetVatValueForMissingValues() {
		assertNull(receiptWithoutOptValues.getVatValue());
	}

}
