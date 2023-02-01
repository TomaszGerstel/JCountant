package com.tgerstel.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tgerstel.calculations.BalanceResults;
import com.tgerstel.calculations.CalculationService;
import com.tgerstel.model.Receipt;
import com.tgerstel.model.Transfer;
import com.tgerstel.model.TransferType;
import com.tgerstel.model.User;
import com.tgerstel.service.ReceiptService;

@WebMvcTest(value = BalanceController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CalculationService calculationService;

	static BalanceResults balanceResult;
	
	@BeforeAll
	static void prepareVariable() {
		balanceResult = new BalanceResults(
				BigDecimal.valueOf(800).setScale(2), BigDecimal.valueOf(1000).setScale(2),
				BigDecimal.valueOf(2000).setScale(2), BigDecimal.valueOf(1600).setScale(2),
				BigDecimal.valueOf(400).setScale(2), BigDecimal.valueOf(200).setScale(2),
				BigDecimal.valueOf(100).setScale(2), null);	
	}
	
	@Test
	void tesCurrentBalance() throws Exception {

		Mockito.when(calculationService.currentBalance(any())).thenReturn(balanceResult);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/current")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("{'costs': 800.00, 'grossIncome': 2000.00,"
						+ " 'profitPaid': 400.00, 'balance': 300.00, 'flatTaxRate': 19.0}"));			
	}
	
	@Test
	void tesBalanceToDataRange() throws Exception {

		Mockito.when(calculationService.balanceToDateRange(any(), any(), any())).thenReturn(balanceResult);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("from", "2022-12-01");
		map.add("to", "2022-12-31");
				
		mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/to_date_range")
				.params(map)
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))		
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("{'grossCosts': 1000.00, 'netIncome': 1600.00,"
						+ " 'vatPaid': 200.00, 'balance': 300.00, 'lumpTaxRate': 12.0}"));			
	}
	
	@Test
	void tesBalanceToCurrentMonth() throws Exception {

		Mockito.when(calculationService.balanceToCurrentMonth(any())).thenReturn(balanceResult);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/to_current_month")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("{'costs': 800.00, 'grossIncome': 2000.00,"
						+ " 'profitRemainingLump': 208.00, 'balance': 300.00, 'vatDue': 200.00}"));			
	}
	
	@Test
	void tesBalanceToLastMonth() throws Exception {

		Mockito.when(calculationService.balanceToLastMonth(any())).thenReturn(balanceResult);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/balance/to_last_month")
				.contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json("{'grossCosts': 1000.00, 'grossIncome': 2000.00,"
						+ " 'netBalance': 800.00, 'vatBalance': 0.00, 'flatTaxRate': 19.0}"));			
	}	
}







