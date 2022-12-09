package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {
	
	@Mock private ReceiptRepository receiptRepo;
	@InjectMocks private ReceiptService receiptService;

	@Test
	void testIfCreateReceiptGivenCorrectValuesWithUserToRepository() {		
	
		Mockito.when(receiptRepo.save(ArgumentMatchers.any())).thenReturn(new Receipt());
		ArgumentCaptor<Receipt> argumentCaptor = ArgumentCaptor.forClass(Receipt.class);
		
		LocalDate dateTime = LocalDate.now();		
		User user = new User("Sober", "sobot@a.com", "hardpass", 13);
		Receipt receipt = new Receipt(dateTime, 810.0f, 200.0f, null, null, "Albatros", "Stan", "for example", null );
		
		receiptService.createReceipt(receipt, user);	
				
		Mockito.verify(receiptRepo).save(argumentCaptor.capture());		
		Receipt receiptReturned = argumentCaptor.getValue();
		
		assertAll(
				() -> assertEquals(810, receiptReturned.getAmount()),
				() -> assertEquals("Sober", receiptReturned.getUser().getUsername())
		);
		

	}

//	@Test
//	void testGetRecentReceipts() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDeleteReceipt() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSearchReceiptsByClientName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testReceiptsInDateRange() {
//		fail("Not yet implemented");
//	}

}
