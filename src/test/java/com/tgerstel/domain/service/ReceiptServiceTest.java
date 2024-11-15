package com.tgerstel.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import org.assertj.core.api.Assertions;
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
import org.springframework.data.domain.PageRequest;

import com.tgerstel.infrastructure.repository.Receipt;
import com.tgerstel.infrastructure.repository.Transfer;
import com.tgerstel.domain.TransferType;
import com.tgerstel.infrastructure.repository.User;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {
	
	@Mock private ReceiptRepository receiptRepo;
	@Mock private TransferRepository transferRepo;
	@InjectMocks private DomainReceiptService receiptService;
	
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
		receipt = new Receipt(dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null, "Albatros", "Stan", "for example", userActual);	
		transfer = new Transfer(TransferType.IN_TRANSFER, BigDecimal.valueOf(800), "Customer", "Me", dateTime,	null, receipt, userActual);		
		user2 = new User("Sober", "sobot@a.com", "hardpass", 13);
		user2.setId(21L);		
		receipt2 = new Receipt(dateTime, BigDecimal.valueOf(1000), BigDecimal.valueOf(250), null, null, "MediaGain", "Ed", "for example", user2);
		transfer2 = new Transfer(TransferType.IN_TRANSFER, BigDecimal.valueOf(1000), "MediaGain", "Me", dateTime, null, receipt2, userActual);		
		receipt3 = new Receipt(dateTime,BigDecimal.valueOf(500), null, null, null, "Stratovarius", "Ed", "for app", userActual);
		receipt.setId(1L);
		receipt2.setId(77L);
		receipt3.setId(3L);
	}

	@Test
	@DisplayName("createReceipt() shoud giving correct values from receipt and with given user to repository method")
	void testCreateReceipt() {		
	
		Mockito.when(receiptRepo.add(ArgumentMatchers.any())).thenReturn(new Receipt());
		ArgumentCaptor<Receipt> argumentCaptor = ArgumentCaptor.forClass(Receipt.class);
			
		receiptService.createReceipt(receipt, userActual);	
				
		Mockito.verify(receiptRepo).add(argumentCaptor.capture());
		Receipt receiptReturned = argumentCaptor.getValue();
		
		assertAll(
				() -> assertEquals(dateTime, receiptReturned.getDate()),
				() -> assertEquals(BigDecimal.valueOf(810).setScale(2), receiptReturned.getAmount()),
				() -> assertEquals("Sober", receiptReturned.getUser().getUsername()),
				() -> assertEquals("sobot@a.com", receiptReturned.getUser().getEmail()),
				() -> assertEquals(13, receiptReturned.getUser().getLumpSumTaxRate()),
				() -> assertEquals(BigDecimal.valueOf(200).setScale(2), receiptReturned.getNetAmount()),
				() -> assertEquals(BigDecimal.valueOf(810).setScale(2), receiptReturned.getAmount()),
				() -> assertEquals("Albatros", receiptReturned.getClient()),
				() -> assertEquals("Stan", receiptReturned.getWorker()),
				() -> assertEquals("for example", receiptReturned.getDescription()),
				() -> assertNull(receiptReturned.getVatPercentage())			
		);
	}

	@Test
	@DisplayName("If getRecentReceipts() giving proper page request and user to repository method")
	void testGetRecentReceipts() {
		
		Mockito.when(receiptRepo.getPageForUser(ArgumentMatchers.any(), ArgumentMatchers.any()))
			.thenReturn(new ArrayList<>());
		
		Integer resultSize = 11;		
		
		ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
		ArgumentCaptor<PageRequest> argumentCaptorPage = ArgumentCaptor.forClass(PageRequest.class);
		
		receiptService.getRecentReceipts(userActual, resultSize);	
		
		Mockito.verify(receiptRepo).getPageForUser(argumentCaptorUser.capture(), argumentCaptorPage.capture());
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
		
		Optional<Receipt> receipt = Optional.of(new Receipt(dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null, 
				"Albatros", "Stan", "for example", userActual ));		
		
		Mockito.when(receiptRepo.getById(1L)).thenReturn(receipt);
		
		Receipt receiptReturned = receiptService.getById(userActual, 1L).get();
		
		assertAll(			
				() -> assertEquals(dateTime, receiptReturned.getDate()),
				() -> assertEquals(BigDecimal.valueOf(810).setScale(2), receiptReturned.getAmount()),
				() -> assertEquals("Sober", receiptReturned.getUser().getUsername()),
				() -> assertEquals("sobot@a.com", receiptReturned.getUser().getEmail()),
				() -> assertEquals(13, receiptReturned.getUser().getLumpSumTaxRate()),
				() -> assertEquals(BigDecimal.valueOf(200).setScale(2), receiptReturned.getNetAmount()),
				() -> assertEquals(BigDecimal.valueOf(810).setScale(2), receiptReturned.getAmount()),
				() -> assertEquals("Albatros", receiptReturned.getClient()),
				() -> assertEquals("Stan", receiptReturned.getWorker()),
				() -> assertEquals("for example", receiptReturned.getDescription()),
				() -> assertNull(receiptReturned.getVatPercentage())			
			);	
	}
	
	@Test
	@DisplayName("If getById() returning empty optional when current user dosn't own found receipt")
	void testIfGetByIdReturnsEmptyOptional() {		
			
		User userActual = new User("Sobek", "sobek@a.com", "hardpass", 12);
		userActual.setId(11L);
		User userOnReceipt = new User("Sober", "sobot@a.com", "hardpass", 13);
		userOnReceipt.setId(21L);
		Optional<Receipt> receipt = Optional.of(new Receipt(dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null, 
				"Albatros", "Stan", "for example", userOnReceipt ));		
		
		Mockito.when(receiptRepo.getById(1L)).thenReturn(receipt);
		Optional<Receipt> receiptReturned = receiptService.getById(userActual, 1L);
		
		Assertions.assertThat(receiptReturned).isEmpty();
}		

	@Test
	void testdeleteReceiptAndRelatedTransfer() {
	
		Mockito.when(receiptRepo.getById(1L)).thenReturn(Optional.of(receipt));
		Mockito.when(transferRepo.getForReceipt(ArgumentMatchers.any())).thenReturn(Optional.of(transfer));
		
		receiptService.deleteReceiptAndRelatedTransfer(userActual, 1L);
		
		Mockito.verify(receiptRepo, Mockito.times(1)).remove(1L);
	}
	
	@Test
	void testDeleteReceipt2() {
		
		Mockito.when(receiptRepo.getById(1L)).thenReturn(Optional.of(receipt2));
		Mockito.when(transferRepo.getForReceipt(ArgumentMatchers.any())).thenReturn(Optional.of(transfer));
		
		receiptService.deleteReceiptAndRelatedTransfer(userActual, 1L);
		
		Mockito.verify(receiptRepo, Mockito.times(0)).remove(1L);
	}

	@Test
	void testReceiptsNotUsedInTransfer() {	

		Transfer transfer = new Transfer();
		Transfer transfer2 = new Transfer();
		transfer.setReceipt(receipt);
		transfer2.setReceipt(receipt3);		
		
		List<Receipt> receipts = List.of(receipt, receipt2, receipt3);
		List<Transfer> transfers = List.of(transfer, transfer2);

		Mockito.when(transferRepo.getAllForUser(ArgumentMatchers.any())).thenReturn(transfers);
		Mockito.when(receiptRepo.getAllForUser(ArgumentMatchers.any())).thenReturn(receipts);
		
		List<Receipt> receiptsReturned = receiptService.receiptsWithoutTransfer(userActual);
		 
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
