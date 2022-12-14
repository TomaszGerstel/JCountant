package com.tgerstel.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.repository.ReceiptRepository;
import com.tgerstel.repository.TransferRepository;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
	
	@Mock private TransferRepository transferRepo;
	@Mock private ReceiptRepository receiptRepo;
	@InjectMocks private TransferService transferService;
	
	static LocalDate dateTime;
	static User userActual;
	static User user2;
	static Receipt receipt;
	static Receipt receiptWithUser2;
	static Transfer transfer;
	static Transfer transferWithReceipt2;
	
	@BeforeAll
	static void prepareVariables() {
		
		dateTime = LocalDate.now();		
		userActual = new User("Bob", "sobob@a.com", "hardpass", 13);
		userActual.setId(1L);
		receipt = new Receipt(dateTime, 1200.0f, 200.0f, null, null, "Customer", "Me", "for example", userActual);
		receipt.setId(22L);
		user2 = new User("Rob", "roby@am.com", "hardpass", 12);
		receiptWithUser2 = new Receipt(dateTime, 1000.0f, 200.0f, null, null, "Sansumg", "Me", "for example", user2);
		transfer = new Transfer(TransferType.IN_TRANSFER, 1200.0f, "Customer", "Me", dateTime,	null, receipt, userActual);
		transferWithReceipt2 = new Transfer(TransferType.IN_TRANSFER, 1200.0f, "Customer", "Me", dateTime,	null, receiptWithUser2, userActual);
	}
	

	@Test
	void testCreateTransferShouldPassProperTransferAndUserToSaveMethod() {	
		
		Mockito.when(transferRepo.save(ArgumentMatchers.any())).thenReturn(new Transfer());
		Mockito.when(receiptRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.of(receipt));
		ArgumentCaptor<Transfer> argumentCaptor = ArgumentCaptor.forClass(Transfer.class);		
		
		transferService.createTransfer(transfer, 22L, userActual);	
				
		Mockito.verify(transferRepo).save(argumentCaptor.capture());		
		Transfer transferReturned = argumentCaptor.getValue();
		
		assertAll(
				() -> assertEquals(dateTime, transferReturned.getDate()),
				() -> assertEquals(1200f, transferReturned.getAmount()),
				() -> assertEquals("Bob", transferReturned.getUser().getUsername()),
				() -> assertNotNull(transferReturned.getUser()),
				() -> assertEquals(TransferType.IN_TRANSFER, transferReturned.getTransferType()),				
				() -> assertEquals("Customer", transferReturned.getFrom()),
				() -> assertEquals("Me", transferReturned.getTo()),
				() -> assertEquals(1200f, transferReturned.getReceipt().getAmount()),
				() -> assertNotNull(transferReturned.getReceipt()),
				() -> assertEquals("Customer", transferReturned.getReceipt().getClient())	
		);
	}
	
	@Test
	void testCreateTransferShouldReturnEmptyOptionalForNotFoundReceipt() {	

		Mockito.when(receiptRepo.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());		
		Optional<Transfer> transferReturned = transferService.createTransfer(transferWithReceipt2, 34L, userActual);	
		
		Assertions.assertThat(transferReturned).isEmpty();	
	}

	@Test
	@DisplayName("getRecentTransfers() shoud giving proper page request and user to repository method")
	void testGetRecentTransfers() {
		
		Mockito.when(transferRepo.findAllByUser(ArgumentMatchers.any(), ArgumentMatchers.any()))
			.thenReturn(new ArrayList<Transfer>());
		
		Integer resultSize = 11;			
		
		ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
		ArgumentCaptor<PageRequest> argumentCaptorPage = ArgumentCaptor.forClass(PageRequest.class);
		
		transferService.getRecentTransfers(userActual, resultSize);	
		
		Mockito.verify(transferRepo).findAllByUser(argumentCaptorUser.capture(), argumentCaptorPage.capture());		
		User capturedUser = argumentCaptorUser.getValue();
		PageRequest capturedPageRequest = argumentCaptorPage.getValue();		
		
		assertAll(
				() -> assertNotNull(capturedUser.getUsername()),
				() -> assertEquals("Bob", capturedUser.getUsername()),		
				() -> assertEquals(resultSize, capturedPageRequest.getPageSize()),
				() -> assertEquals("date: DESC", capturedPageRequest.getSort().toString())
		);	
	}
	@Test
	@DisplayName("If getById() returning proper transfer for current user")
	void testIfGetByIdReturnsTransfer() {			
		
		Mockito.when(transferRepo.findById(1L)).thenReturn(Optional.of(transfer));
		
		Transfer transferReturned = transferService.getById(userActual, 1L).get();
		
		assertAll(			
				() -> assertEquals(dateTime, transferReturned.getDate()),
				() -> assertEquals(1200f, transferReturned.getAmount()),
				() -> assertEquals("Bob", transferReturned.getUser().getUsername()),
				() -> assertEquals(TransferType.IN_TRANSFER, transferReturned.getTransferType())
	
			);	
	}
	
	@Test
	@DisplayName("If getById() returning empty optional when current user dosn't own found transfer")
	void testIfGetByIdReturnsEmptyOptional() {
		
		Mockito.when(transferRepo.findById(1L)).thenReturn(Optional.of(transfer));		
		Optional<Transfer> receiptReturned = transferService.getById(user2, 1L);
		
		Assertions.assertThat(receiptReturned).isEmpty();
}	
	
	@Test
	void testDeleteTransfer() {
		
		Mockito.when(transferRepo.findById(1L)).thenReturn(Optional.of(transfer));	
		
		transferService.deleteTransfer(userActual, 1L);
		
		Mockito.verify(transferRepo, Mockito.times(1)).deleteById(1L);		
	
	}
	
	@Test
	void testDeleteTransfer2() {
				
		
		Mockito.when(transferRepo.findById(1L)).thenReturn(Optional.of(transfer));	
		
		transferService.deleteTransfer(user2, 1L);
		
		Mockito.verify(transferRepo, Mockito.times(0)).deleteById(1L);		
	
	}

}
