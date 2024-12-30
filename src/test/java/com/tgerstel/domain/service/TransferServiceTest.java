package com.tgerstel.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateTransferCommand;
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

import com.tgerstel.domain.TransferType;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepo;

    @Mock
    private ReceiptRepository receiptRepo;

    @InjectMocks
    private DomainTransferService transferService;

    static LocalDate dateTime;
    static User userActual;
    static User user2;
    static Receipt receipt;
    static Receipt receiptWithUser2;
    static Transfer transfer;
    static Transfer transferWithReceipt2;

    static CreateTransferCommand createTransferCommand;
    static CreateTransferCommand createTransferCommand2;

    @BeforeAll
    static void prepareVariables() {

        dateTime = LocalDate.now();
        userActual = new User(1L, "Bob", "sobob@a.com", "hardpass", 13, Set.of());
        userActual.setId(1L);
        receipt = new Receipt(22L, dateTime, BigDecimal.valueOf(1200), BigDecimal.valueOf(200.0), null, null, "Customer", "Me", "for example", userActual);
        user2 = new User(2L, "Rob", "roby@am.com", "hardpass", 12, Set.of());
        receiptWithUser2 = new Receipt(33L, dateTime, BigDecimal.valueOf(1000), BigDecimal.valueOf(200.0), null, null, "Sansumg", "Me", "for example", user2);
        transfer = new Transfer(122L, TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null, null, receipt, userActual);
        transferWithReceipt2 = new Transfer(133L, TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null, null, receiptWithUser2, userActual);

        createTransferCommand = new CreateTransferCommand(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null, receipt.getId(), userActual);
        createTransferCommand2 = new CreateTransferCommand(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null, receiptWithUser2.getId(), userActual);
    }


    @Test
    @DisplayName("createTransferShouldPassProperTransferAndUserToSaveMethod")
    void testCreateTransfer() {
        // given
        Mockito.when(transferRepo.add(ArgumentMatchers.any())).thenReturn(transfer);
        Mockito.when(receiptRepo.getById(ArgumentMatchers.any())).thenReturn(Optional.of(receipt));
        ArgumentCaptor<CreateTransferCommand> argumentCaptor = ArgumentCaptor.forClass(CreateTransferCommand.class);

        // when
        transferService.createTransfer(createTransferCommand);

        // then
        Mockito.verify(transferRepo).add(argumentCaptor.capture());
        CreateTransferCommand captured = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(dateTime, captured.date()),
                () -> assertEquals(BigDecimal.valueOf(1200), captured.amount()),
                () -> assertEquals("Bob", captured.user().getUsername()),
                () -> assertNotNull(captured.user()),
                () -> assertEquals(TransferType.IN_TRANSFER, captured.transferType()),
                () -> assertEquals("Customer", captured.from()),
                () -> assertEquals("Me", captured.to()),
                () -> assertEquals(22L, captured.receiptId())
        );
    }

    @Test
    @DisplayName("createTransferShouldReturnEmptyOptionalForNotFoundReceipt")
    void testCreateTransferForNotExistingReceipt() {
        // given
        Mockito.when(receiptRepo.getById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        // when
        Optional<Transfer> transferReturned = transferService.createTransfer(createTransferCommand2);

        // then
        Assertions.assertThat(transferReturned).isEmpty();
    }

    @Test
    @DisplayName("shouldCreateProperPageRequestToRepositoryForRecentTransfers")
    void testGetRecentTransfers() {
        // given
        Mockito.when(transferRepo.getPageForUser(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(new ArrayList<>());
        Integer resultSize = 11;

        ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<PageRequest> argumentCaptorPage = ArgumentCaptor.forClass(PageRequest.class);

        // when
        transferService.getRecentTransfers(userActual, resultSize);

        // then
        Mockito.verify(transferRepo).getPageForUser(argumentCaptorUser.capture(), argumentCaptorPage.capture());
        User capturedUser = argumentCaptorUser.getValue();
        PageRequest capturedPageRequest = argumentCaptorPage.getValue();

        assertAll(
                () -> assertNotNull(capturedUser.getUsername()),
                () -> assertEquals("Bob", capturedUser.getUsername()),
                () -> assertEquals(resultSize, capturedPageRequest.getPageSize()),
                () -> assertEquals("baseDate: DESC", capturedPageRequest.getSort().toString())
        );
    }

    @Test
    @DisplayName("shouldReturnProperTransferForCurrentUser")
    void testGetTransferById() {
        // given
        Mockito.when(transferRepo.getById(1L)).thenReturn(Optional.of(transfer));

        // when
        Transfer transferReturned = transferService.getById(userActual, 1L).get();

        // then
        assertAll(
                () -> assertEquals(dateTime, transferReturned.getDate()),
                () -> assertEquals(BigDecimal.valueOf(1200).setScale(2), transferReturned.getAmount()),
                () -> assertEquals("Bob", transferReturned.getUser().getUsername()),
                () -> assertEquals(TransferType.IN_TRANSFER, transferReturned.getTransferType())
        );
    }

    @Test
    @DisplayName("shouldReturnEmptyOptionalWhenCurrentUserDoesn'tOwnTransfer")
    void testGetTransferReturnsEmptyOptional() {
        // given
        Mockito.when(transferRepo.getById(1L)).thenReturn(Optional.of(transfer));

        // when
        Optional<Transfer> receiptReturned = transferService.getById(user2, 1L);

        // then
        Assertions.assertThat(receiptReturned).isEmpty();
    }

    @Test
    void testDeleteTransfer() {
        // given
        Mockito.when(transferRepo.getById(1L)).thenReturn(Optional.of(transfer));

        // when
        transferService.deleteTransfer(userActual, 1L);

        // then
        Mockito.verify(transferRepo, Mockito.times(1)).remove(1L);
    }

    @Test
    @DisplayName("shouldNotTryToDeleteTransferIfCurrentUserNotOwnsIt")
    void testDeleteTransferForAnotherUser() {
        // given
        Mockito.when(transferRepo.getById(1L)).thenReturn(Optional.of(transfer));

        // when
        transferService.deleteTransfer(user2, 1L);

        // then
        Mockito.verify(transferRepo, Mockito.times(0)).remove(1L);
    }

}
