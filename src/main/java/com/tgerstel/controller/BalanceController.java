package com.tgerstel.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tgerstel.model.BalanceResults;
import com.tgerstel.model.User;
import com.tgerstel.service.CalculationService;


@RestController
@RequestMapping(path="/api/balance", produces="application/json")
@CrossOrigin(origins="*")
public class BalanceController {
	
	private final CalculationService calculationService;	

	public BalanceController(CalculationService calculationService) {
		this.calculationService = calculationService;
	}

	@GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BalanceResults> allReceipts(@AuthenticationPrincipal User user) {		
		
		return ResponseEntity.ok(calculationService.currentBalance(user));
	}

	
	
}
