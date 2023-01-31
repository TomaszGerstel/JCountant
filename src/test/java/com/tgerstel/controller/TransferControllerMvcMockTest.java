package com.tgerstel.controller;

import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.service.TransferService;

@WebMvcTest(value = TransferController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ExtendWith(MockitoExtension.class)
public class TransferControllerMvcMockTest {

	@MockBean
	TransferService transferService;

	@Autowired
	private MockMvc mockMvc;

	static LocalDate dateTime;
	static Receipt receipt;
	static Transfer transfer;
	static Transfer specialTransfer;
	static Transfer notValidTransfer;
	static List<Transfer> transfers;

	@BeforeAll
	static void prepareVariables() {

		dateTime = LocalDate.now();
		receipt = new Receipt(dateTime, BigDecimal.valueOf(1200), BigDecimal.valueOf(1000.0), null, null, "Customer",
				"Me", "for example", null);
		transfer = new Transfer(TransferType.IN_TRANSFER, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null,
				receipt, null);
		transfer.setId(12L);
		specialTransfer = new Transfer(TransferType.SALARY, BigDecimal.valueOf(1200), "Customer", "Me", dateTime, null,
				null, null);
		specialTransfer.setId(33L);
		notValidTransfer = new Transfer(TransferType.TAX_OUT_TRANSFER, BigDecimal.valueOf(200), "Customer", "Me", null,
				null, null, null);
		transfers = List.of(transfer, specialTransfer);
	}

	@Test
	void addTransferTest() throws Exception {

		Mockito.when(transferService.createTransfer(any(), any(), any())).thenReturn(Optional.of(transfer));

		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/transfer").content(asJsonString(transfer)).param("receiptId", "2")
						.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().json("{'amount': 1200.00}"))
				.andExpect(MockMvcResultMatchers.content().json("{'id': 12}"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	@DisplayName("AddTransfer with regular transfer type without receipt_id should return 422 status")
	void addTransferTest2() throws Exception {

		Mockito.when(transferService.createTransfer(any(), any(), any())).thenReturn(Optional.of(transfer));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/transfer").content(asJsonString(transfer))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	@DisplayName("AddTransfer with special transfer type without receipt_id should save transfer")
	void addTransferTest3() throws Exception {

		Mockito.when(transferService.createTransfer(any(), any(), any())).thenReturn(Optional.of(specialTransfer));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/transfer").content(asJsonString(specialTransfer))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.content().json("{'amount': 1200.00}"))
				.andExpect(MockMvcResultMatchers.content().json("{'id': 33}"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
	}

	@Test
	@DisplayName("AddTransfer with no valid transfer")
	void addTransferTest4() throws Exception {

		Mockito.when(transferService.createTransfer(any(), any(), any())).thenReturn(Optional.of(notValidTransfer));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/transfer").content(asJsonString(notValidTransfer))
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}
	
	@Test
	void testAllTransfers() throws Exception {

		Mockito.when(transferService.getRecentTransfers(any(),any()))
				.thenReturn(transfers);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transfer/recent")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.content().json("[{'amount': 1200.00}, {'amount': 1200.00}]"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	// TODO : check, ok? and delete methods
	@Test
	void testGetTransferById() throws Exception {

		Mockito.when(transferService.getById(any(),anyLong()))
				.thenReturn(Optional.of(transfer));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/transfer/1")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.content().json("{'amount': 1200.00}"))
				.andExpect(MockMvcResultMatchers.content().json("{'id': 12}"))
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

}
