package com.tgerstel.controller;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tgerstel.model.User;
import com.tgerstel.model.Receipt;
import com.tgerstel.service.ReceiptService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(path="/api/balance", produces="application/json")
@CrossOrigin(origins="*")
public class BalanceController {
	
	private final ReceiptService receiptService;

	public BalanceController(ReceiptService receiptService) {		
		this.receiptService = receiptService;
	}
	


	@GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Receipt>> allReceipts(@RequestParam String from, @RequestParam String to, 
			@AuthenticationPrincipal User user) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");	
		
		LocalDate fromd = LocalDate.parse(from, formatter);
		LocalDate tod = LocalDate.parse(to, formatter);
		
		
		List<Receipt> allReceipts = receiptService.receiptsInDateRange(user, fromd, tod);
		return ResponseEntity.ok(allReceipts);
	}

	
	
}
