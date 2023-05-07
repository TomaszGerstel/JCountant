package com.tgerstel.controller;

import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tgerstel.calculations.BalanceResults;
import com.tgerstel.calculations.CalculationService;
import com.tgerstel.model.User;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@OpenAPIDefinition(info = @Info(title = "Receipt API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
@RequestMapping(path = "/api/balance", produces = "application/json")
@CrossOrigin(origins = "*")
public class BalanceController {

	private final CalculationService calculationService;

	public BalanceController(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

	@GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BalanceResults> currentBalance(@AuthenticationPrincipal User user) {		
		return ResponseEntity.ok(calculationService.currentBalance(user));
	}

	@GetMapping(path = "/to_date_range", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BalanceResults> balanceToDateRange(@RequestParam LocalDate from, @RequestParam LocalDate to,
			@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(calculationService.balanceToDateRange(from, to, user));
	}

	@GetMapping(path = "/to_current_month", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BalanceResults> balanceToCurrentMonth(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(calculationService.balanceToCurrentMonth(user));
	}

	@GetMapping(path = "/to_last_month", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BalanceResults> balanceToLastMonth(@AuthenticationPrincipal User user) {
		return ResponseEntity.ok(calculationService.balanceToLastMonth(user));
	}

}
