package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {
	
	@Mock private ReceiptRepository receiptRepo;
	@InjectMocks private ReceiptService receiptService;

	@Test
	@DisplayName("If createReceipt() giving correct values from receipt and with given user to repository method")
	void testCreateReceipt() {		
	
		Mockito.when(receiptRepo.save(ArgumentMatchers.any())).thenReturn(new Receipt());
		ArgumentCaptor<Receipt> argumentCaptor = ArgumentCaptor.forClass(Receipt.class);
		
		LocalDate dateTime = LocalDate.now();		
		User user = new User("Sober", "sobot@a.com", "hardpass", 13);
		Receipt receipt = new Receipt(dateTime, 810.0f, 200.0f, null, null, "Albatros", "Stan", "for example", null );
		
		receiptService.createReceipt(receipt, user);	
				
		Mockito.verify(receiptRepo).save(argumentCaptor.capture());		
		Receipt receiptReturned = argumentCaptor.getValue();
		
		assertAll(
				() -> assertEquals(dateTime, receiptReturned.getDate()),
				() -> assertEquals(810f, receiptReturned.getAmount()),
				() -> assertEquals("Sober", receiptReturned.getUser().getUsername()),
				() -> assertEquals("sobot@a.com", receiptReturned.getUser().getEmail()),
				() -> assertEquals(13, receiptReturned.getUser().getLumpSumTaxRate()),
				() -> assertEquals(200.0f, receiptReturned.getNetAmount()),
				() -> assertEquals(810, receiptReturned.getAmount()),
				() -> assertEquals("Albatros", receiptReturned.getClient()),
				() -> assertEquals("Stan", receiptReturned.getWorker()),
				() -> assertEquals("for example", receiptReturned.getDescription()),
				() -> assertNull(receiptReturned.getVatPercentage()),
				() -> assertNull(receiptReturned.getVatValue())
		);
	}

	@Test
	@DisplayName("If getRecentReceipts() giving proper page request and user to repository method")
	void testGetRecentReceipts() {
		
		Mockito.when(receiptRepo.findAllByUser(ArgumentMatchers.any(), ArgumentMatchers.any()))
			.thenReturn(new ArrayList<Receipt>());
		
		Integer resultSize = 11;
		User user = new User("Sober", "sobot@a.com", "hardpass", 13);
		
		
		ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
		ArgumentCaptor<PageRequest> argumentCaptorPage = ArgumentCaptor.forClass(PageRequest.class);
		
		receiptService.getRecentReceipts(user, resultSize);	
		
		Mockito.verify(receiptRepo).findAllByUser(argumentCaptorUser.capture(), argumentCaptorPage.capture());		
		User capturedUser = argumentCaptorUser.getValue();
		PageRequest capturedPageRequest = argumentCaptorPage.getValue();		
		
		assertAll(
				() -> assertNotNull(capturedUser.getUsername()),
				() -> assertEquals("Sober", capturedUser.getUsername()),		
				() -> assertEquals(resultSize, capturedPageRequest.getPageSize()),
				() -> assertEquals("date: DESC", capturedPageRequest.getSort().toString())
		);	
	}

	@Test
	@DisplayName("If getById() returning proper receipt for current user")
	void testIfGetByIdRetursReceipt() {
		
		LocalDate dateTime = LocalDate.now();		
		User user = new User("Sober", "sobot@a.com", "hardpass", 13);
		user.setId(11L);
		Optional<Receipt> receipt = Optional.of(new Receipt(dateTime, 810.0f, 200.0f, null, null, 
				"Albatros", "Stan", "for example", user ));		
		
		Mockito.when(receiptRepo.findById(1L)).thenReturn(receipt);
		
		Receipt receiptReturned = receiptService.getById(user, 1L).get();
		
		assertAll(			
				() -> assertEquals(dateTime, receiptReturned.getDate()),
				() -> assertEquals(810f, receiptReturned.getAmount()),
				() -> assertEquals("Sober", receiptReturned.getUser().getUsername()),
				() -> assertEquals("sobot@a.com", receiptReturned.getUser().getEmail()),
				() -> assertEquals(13, receiptReturned.getUser().getLumpSumTaxRate()),
				() -> assertEquals(200.0f, receiptReturned.getNetAmount()),
				() -> assertEquals(810, receiptReturned.getAmount()),
				() -> assertEquals("Albatros", receiptReturned.getClient()),
				() -> assertEquals("Stan", receiptReturned.getWorker()),
				() -> assertEquals("for example", receiptReturned.getDescription()),
				() -> assertNull(receiptReturned.getVatPercentage()),
				() -> assertNull(receiptReturned.getVatValue())
			);	
	}
	
	@Test
	@DisplayName("If getById() returning empty optional when current user dosn't own found receipt")
	void testIfGetByIdReturnsEmptyOptional() {
		
		LocalDate dateTime = LocalDate.now();		
		User userActual = new User("Sobek", "sobek@a.com", "hardpass", 12);
		userActual.setId(11L);
		User userOnReceipt = new User("Sober", "sobot@a.com", "hardpass", 13);
		userActual.setId(21L);
		Optional<Receipt> receipt = Optional.of(new Receipt(dateTime, 810.0f, 200.0f, null, null, 
				"Albatros", "Stan", "for example", userOnReceipt ));		
		
		Mockito.when(receiptRepo.findById(1L)).thenReturn(receipt);		
		Optional<Receipt> receiptReturned = receiptService.getById(userActual, 1L);
		
		Assertions.assertThat(receiptReturned).isEmpty();
}
	
	
	
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
