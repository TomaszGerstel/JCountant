package com.tgerstel.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.service.ReceiptService;


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

	@BeforeAll
	static void prepareVariables() {
		
		
		dateTime = LocalDate.now();
		userActual = new User("Bob", "sobob@a.com", "hardpass", 13);
		userActual.setId(1L);
		receipt = new Receipt(dateTime, BigDecimal.valueOf(1200), BigDecimal.valueOf(200), null, null, "Customer", "Me", "for example", userActual);
		receipt.setId(22L);
		user2 = new User("Rob", "roby@am.com", "hardpass", 12);
		receiptWithUser2 = new Receipt(dateTime, BigDecimal.valueOf(1000), BigDecimal.valueOf(200), null, null, "Sansumg", "Me", "for example", user2);
		transfer = new Transfer(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null, receipt,
				userActual);
		transferWithReceipt2 = new Transfer(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null,
				receiptWithUser2, userActual);
	}

	@Test
	void testAddReceipt() throws Exception {
		
		Mockito.when(receiptService.createReceipt(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(receipt);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/receipt")
				.content(asJsonString(receipt))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
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

//	@Test
//	void testGetReceiptById() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testDelete() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSearchReceipts() {
//		fail("Not yet implemented");
//	}

}

