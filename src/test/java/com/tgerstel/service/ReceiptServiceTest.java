package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;
import com.tgerstel.repository.TransferRepository;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {
	
	@Mock private ReceiptRepository receiptRepo;
	@Mock private TransferRepository transferRepo;
	@InjectMocks private ReceiptService receiptService;
	
	static LocalDate dateTime;
	static User userActual;
	static Receipt receipt;
	static Transfer transfer;
	static User user2;
	static Receipt receipt2;
	static Transfer transfer2;
	static List<Long> receiptsId;
	static Receipt receipt3;
	
	@BeforeAll
	static void prepareVariables() {
		
		dateTime = LocalDate.now();
		userActual = new User("Sober", "sobot@a.com", "hardpass", 13);
		userActual.setId(11L);
		receipt = new Receipt(dateTime, 810.0f, 200.0f, null, null, "Albatros", "Stan", "for example", userActual);	
		transfer = new Transfer(TransferType.IN_TRANSFER, 800.0f, "Customer", "Me", dateTime,	null, receipt, userActual);		
		user2 = new User("Sober", "sobot@a.com", "hardpass", 13);
		user2.setId(21L);		
		receipt2 = new Receipt(dateTime, 1000.0f, 250.0f, null, null, "MediaGain", "Ed", "for example", user2);
		transfer2 = new Transfer(TransferType.IN_TRANSFER, 1000.0f, "MediaGain", "Me", dateTime, null, receipt2, userActual);		
		receipt3 = new Receipt(dateTime, 500.0f, null, null, null, "Stratovarius", "Ed", "for app", userActual);
		receipt.setId(1L);
		receipt2.setId(77L);
		receipt3.setId(3L);
	}

	@Test
	@DisplayName("createReceipt() shoud giving correct values from receipt and with given user to repository method")
	void testCreateReceipt() {		
	
		Mockito.when(receiptRepo.save(ArgumentMatchers.any())).thenReturn(new Receipt());
		ArgumentCaptor<Receipt> argumentCaptor = ArgumentCaptor.forClass(Receipt.class);
			
		receiptService.createReceipt(receipt, userActual);	
				
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
		
		ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
		ArgumentCaptor<PageRequest> argumentCaptorPage = ArgumentCaptor.forClass(PageRequest.class);
		
		receiptService.getRecentReceipts(userActual, resultSize);	
		
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
		
		Optional<Receipt> receipt = Optional.of(new Receipt(dateTime, 810.0f, 200.0f, null, null, 
				"Albatros", "Stan", "for example", userActual ));		
		
		Mockito.when(receiptRepo.findById(1L)).thenReturn(receipt);
		
		Receipt receiptReturned = receiptService.getById(userActual, 1L).get();
		
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
			
		User userActual = new User("Sobek", "sobek@a.com", "hardpass", 12);
		userActual.setId(11L);
		User userOnReceipt = new User("Sober", "sobot@a.com", "hardpass", 13);
		userOnReceipt.setId(21L);
		Optional<Receipt> receipt = Optional.of(new Receipt(dateTime, 810.0f, 200.0f, null, null, 
				"Albatros", "Stan", "for example", userOnReceipt ));		
		
		Mockito.when(receiptRepo.findById(1L)).thenReturn(receipt);		
		Optional<Receipt> receiptReturned = receiptService.getById(userActual, 1L);
		
		Assertions.assertThat(receiptReturned).isEmpty();
}		

	@Test
	void testdeleteReceiptAndRelatedTransfer() {
	
		Mockito.when(receiptRepo.findById(1L)).thenReturn(Optional.of(receipt));
		Mockito.when(transferRepo.findByReceipt(ArgumentMatchers.any())).thenReturn(Optional.of(transfer));
		
		receiptService.deleteReceiptAndRelatedTransfer(userActual, 1L);
		
		Mockito.verify(receiptRepo, Mockito.times(1)).deleteById(1L);	
	}
	
	@Test
	void testDeleteReceipt2() {
		
		Mockito.when(receiptRepo.findById(1L)).thenReturn(Optional.of(receipt2));
		Mockito.when(transferRepo.findByReceipt(ArgumentMatchers.any())).thenReturn(Optional.of(transfer));
		
		receiptService.deleteReceiptAndRelatedTransfer(userActual, 1L);
		
		Mockito.verify(receiptRepo, Mockito.times(0)).deleteById(1L);	
	}

	@Test
	void testReceiptsNotUsedInTransfer() {	

		Transfer transfer = new Transfer();
		Transfer transfer2 = new Transfer();
		transfer.setReceipt(receipt);
		transfer2.setReceipt(receipt3);		
		
		List<Receipt> receipts = List.of(receipt, receipt2, receipt3);
		List<Transfer> transfers = List.of(transfer, transfer2);

		Mockito.when(transferRepo.findAllByUser(ArgumentMatchers.any())).thenReturn(transfers);
		Mockito.when(receiptRepo.findAllByUser(ArgumentMatchers.any())).thenReturn(receipts);
		
		List<Receipt> receiptsReturned = receiptService.receiptsNotUsedInTransfer(userActual);
		 
		assertAll(				
				() -> assertEquals(1, receiptsReturned.size()),
				() -> assertTrue(receiptsReturned.contains(receipt2)),
				() -> assertFalse(receiptsReturned.contains(receipt3)),
				() -> assertFalse(receiptsReturned.contains(receipt))			
			);		
	}
	
	
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
