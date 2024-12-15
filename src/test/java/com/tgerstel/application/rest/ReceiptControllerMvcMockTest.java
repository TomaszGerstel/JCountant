package com.tgerstel.application.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.ReceiptService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tgerstel.domain.TransferType;

@WebMvcTest(value = ReceiptController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
class ReceiptControllerMvcMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiptService receiptService;

    static LocalDate dateTime;
    static User userActual;
    static User user2;
    static Receipt receipt;
    static Receipt receiptWithUser2;
    static Transfer transfer;
    static Transfer transferWithReceipt2;
    static List<Receipt> receipts;

    @BeforeAll
    static void prepareVariables() {

        dateTime = LocalDate.now();
        userActual = new User(1L, "Bob", "sobob@a.com", "hardpass", 13, Set.of());
        receipt = new Receipt(22L, dateTime, BigDecimal.valueOf(1200), BigDecimal.valueOf(200), null, null, "Customer", "Me",
                "for example", userActual);
        user2 = new User(2L, "Rob", "roby@am.com", "hardpass", 12, Set.of());
        receiptWithUser2 = new Receipt(23L, dateTime, BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null, null,
                "Sansumg", "Me", "for example", user2);
        transfer = new Transfer(12L, TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null, null,
                receipt, userActual);
        transferWithReceipt2 = new Transfer(13L, TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me",
                dateTime, null, null, receiptWithUser2, userActual);
        receipts = List.of(receipt, receiptWithUser2);
    }

    @Test
    void testAddReceipt() throws Exception {

        Mockito.when(receiptService.createReceipt(any())).thenReturn(receipt);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/receipt").content(asJsonString(receipt))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("{'amount': 1200.00}"))
                .andExpect(MockMvcResultMatchers.content().json("{'id': 22}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    void testAllReceipts() throws Exception {

        Mockito.when(receiptService.getRecentReceipts(any(), any()))
                .thenReturn(receipts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipt/recent")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json("[{'amount': 1200.00}, {'amount': 1000.00}]"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testNoTransferReceipts() throws Exception {

        Mockito.when(receiptService.receiptsWithoutTransfer(any())).thenReturn(receipts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipt/no_transfer_receipts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json("[{'amount': 1200.00}, {'amount': 1000.00}]"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public static String asJsonString(final Object obj) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetReceiptById() throws Exception {

        Mockito.when(receiptService.getById(any(), anyLong()))
                .thenReturn(Optional.of(receipt));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipt/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json("{'amount': 1200.00}"))
                .andExpect(MockMvcResultMatchers.content().json("{'id': 22}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("GetReceiptById should return not found status if there is no result")
    void testGetReceiptById2() throws Exception {

        Mockito.when(receiptService.getById(any(), eq(1L)))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipt/1").contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {

//		Mockito.doNothing().when(receiptService).deleteReceiptAndRelatedTransfer(ArgumentMatchers.any(),
//				ArgumentMatchers.eq(1L));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/receipt/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testSearchReceipts() throws Exception {

        Mockito.when(receiptService.searchReceiptsForClientName(any(), eq("client_name")))
                .thenReturn(receipts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipt/search")
                        .param("key", "client_name")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json("[{'amount': 1200.00}, {'amount': 1000.00}]"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("SearchReceipt should return ok status i empty result if there no receipts found")
    void testSearchReceipts2() throws Exception {

        Mockito.when(receiptService.searchReceiptsForClientName(any(), eq("some_client")))
                .thenReturn(receipts);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipt/search")
                        .param("key", "client_name")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().json("[]"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
