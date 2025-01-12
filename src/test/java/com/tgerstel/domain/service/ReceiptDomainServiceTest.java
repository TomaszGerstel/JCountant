package com.tgerstel.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
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
class ReceiptDomainServiceTest {

    @Mock
    private ReceiptRepository receiptRepo;
    @Mock
    private TransferRepository transferRepo;
    @InjectMocks
    private DomainReceiptService receiptService;

    static LocalDate dateTime;
    static User userActual;
    static Receipt receipt;
    static Transfer transfer;
    static User user2;
    static Receipt receipt2;
    static Transfer transfer2;
    static Receipt receipt3;

    static CreateReceiptCommand createReceiptCommand;

    @BeforeAll
    static void prepareVariables() {

        dateTime = LocalDate.now();
        userActual = new User(1L, "Sober", "sobot@a.com", "hardpass", 13, Set.of());
        receipt = new Receipt(1L, dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null, "Albatros", "Stan", "for example", userActual);
        transfer = new Transfer(11L, TransferType.IN_TRANSFER, BigDecimal.valueOf(800), "Customer", "Me", dateTime, null, null, receipt, userActual);
        user2 = new User(2L, "Sober", "sobot@a.com", "hardpass", 13, Set.of());
        receipt2 = new Receipt(77L, dateTime, BigDecimal.valueOf(1000), BigDecimal.valueOf(250), null, null, "MediaGain", "Ed", "for example", user2);
        transfer2 = new Transfer(177L, TransferType.IN_TRANSFER, BigDecimal.valueOf(1000), "MediaGain", "Me", dateTime, null, null, receipt2, userActual);
        receipt3 = new Receipt(3L, dateTime, BigDecimal.valueOf(500), null, null, null, "Stratovarius", "Ed", "for app", userActual);

        createReceiptCommand = new CreateReceiptCommand(dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null, "Albatros", "Stan", "for example", userActual);
    }

    @Test
    @DisplayName("should return correct values from receipt")
    void testCreateReceipt() {
        // given
        Mockito.when(receiptRepo.add(ArgumentMatchers.any())).thenReturn(null);
        ArgumentCaptor<CreateReceiptCommand> argumentCaptor = ArgumentCaptor.forClass(CreateReceiptCommand.class);

        // when
        receiptService.createReceipt(createReceiptCommand);

        // then
        Mockito.verify(receiptRepo).add(argumentCaptor.capture());
        CreateReceiptCommand captured = argumentCaptor.getValue();

        assertAll(
                () -> assertEquals(dateTime, captured.date()),
                () -> assertEquals(BigDecimal.valueOf(810), captured.amount()),
                () -> assertEquals("Sober", captured.user().getUsername()),
                () -> assertEquals("sobot@a.com", captured.user().getEmail()),
                () -> assertEquals(13, captured.user().getLumpSumTaxRate()),
                () -> assertEquals(BigDecimal.valueOf(200), captured.netAmount()),
                () -> assertEquals(BigDecimal.valueOf(810), captured.amount()),
                () -> assertEquals("Albatros", captured.client()),
                () -> assertEquals("Stan", captured.worker()),
                () -> assertEquals("for example", captured.description()),
                () -> assertNull(captured.vatPercentage())
        );
    }

    @Test
    @DisplayName("should create proper request to repository")
    void testGetRecentReceipts() {
        // given
        Mockito.when(receiptRepo.getPageForUser(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(new ArrayList<>());
        Integer resultSize = 11;

        ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<PageRequest> argumentCaptorPage = ArgumentCaptor.forClass(PageRequest.class);

        // when
        receiptService.getRecentReceipts(userActual, resultSize);

        // then
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
    @DisplayName("should return proper receipt for current user")
    void testGetReceiptById() {
        // given
        Optional<Receipt> receipt = Optional.of(new Receipt(123L, dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null,
                "Albatros", "Stan", "for example", userActual));

        Mockito.when(receiptRepo.getById(1L)).thenReturn(receipt);

        // when
        Receipt receiptReturned = receiptService.getById(userActual, 1L).get();

        // then
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
    @DisplayName("should return empty optional when current user doesn't own receipt")
    void testGetReceiptByIdForNotExistingReceipt() {
        // given
        User userActual = new User(1L, "Sobek", "sobek@a.com", "hardpass", 12, Set.of());
        userActual.setId(11L);
        User userOnReceipt = new User(2L, "Sober", "sobot@a.com", "hardpass", 13, Set.of());
        userOnReceipt.setId(21L);
        Optional<Receipt> receipt = Optional.of(new Receipt(123L, dateTime, BigDecimal.valueOf(810), BigDecimal.valueOf(200), null, null,
                "Albatros", "Stan", "for example", userOnReceipt));

        Mockito.when(receiptRepo.getById(1L)).thenReturn(receipt);

        // when
        Optional<Receipt> receiptReturned = receiptService.getById(userActual, 1L);

        // then
        Assertions.assertThat(receiptReturned).isEmpty();
    }

    @Test
    @DisplayName("should delete receipt and related transfer")
    void testDeleteReceiptAndRelatedTransfer() {
        // given
        Mockito.when(receiptRepo.getById(1L)).thenReturn(Optional.of(receipt));
        Mockito.when(transferRepo.getForReceipt(ArgumentMatchers.any())).thenReturn(Optional.of(transfer));

        // when
        receiptService.deleteReceiptAndRelatedTransfer(userActual, 1L);

        // then
        Mockito.verify(receiptRepo, Mockito.times(1)).remove(1L);
    }

    @Test
    @DisplayName("should not delete receipt when current user doesn't own given receipt")
    void testDeleteReceipt() {
        // given
        Mockito.when(receiptRepo.getById(1L)).thenReturn(Optional.of(receipt2));

        // when
        receiptService.deleteReceiptAndRelatedTransfer(userActual, 1L);

        // then
        Mockito.verify(receiptRepo, Mockito.times(0)).remove(1L);
    }

    @Test
    @DisplayName("should return receipts not used in any transfer")
    void testReceiptsNotUsedInTransfer() {
        // given
        Transfer transfer = new Transfer(1L, TransferType.IN_TRANSFER, BigDecimal.valueOf(222), "Customer", "Me", dateTime, null, null, receipt, userActual);
        Transfer transfer2 = new Transfer(2L, TransferType.IN_TRANSFER, BigDecimal.valueOf(333), "Customer", "Me", dateTime, null, null, receipt3, userActual);

        List<Receipt> receipts = List.of(receipt, receipt2, receipt3);
        List<Transfer> transfers = List.of(transfer, transfer2);

        Mockito.when(transferRepo.getAllForUser(ArgumentMatchers.any())).thenReturn(transfers);
        Mockito.when(receiptRepo.getAllForUser(ArgumentMatchers.any())).thenReturn(receipts);

        // when
        List<Receipt> receiptsReturned = receiptService.receiptsWithoutTransfer(userActual);

        // then
        assertAll(
                () -> assertEquals(1, receiptsReturned.size()),
                () -> assertTrue(receiptsReturned.contains(receipt2)),
                () -> assertFalse(receiptsReturned.contains(receipt3)),
                () -> assertFalse(receiptsReturned.contains(receipt))
        );
    }

}
