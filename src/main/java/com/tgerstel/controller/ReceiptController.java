package com.tgerstel.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
@RequestMapping(path="/api/receipt", produces="application/json")
@CrossOrigin(origins="*")
public class ReceiptController {
	
	private final ReceiptService receiptService;

	public ReceiptController(ReceiptService receiptService) {		
		this.receiptService = receiptService;
	}
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addReceipt(@RequestBody @Valid Receipt receipt, Errors errors,
			@AuthenticationPrincipal User user) {

		if (errors.hasErrors()) {
			List<String> errorMessage = errors.getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());			
			return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Receipt saved = receiptService.createReceipt(receipt, user);
		URI savedReceiptLocation = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.getId())
				.toUri();
		
		return ResponseEntity.created(savedReceiptLocation).body(saved);
	}
	
	@GetMapping(path = "/recent", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Receipt>> allReceipts(@RequestParam(defaultValue = "10") Integer resultSize,
			@AuthenticationPrincipal User user) {
		
		List<Receipt> allReceipts = receiptService.getRecentReceipts(user, resultSize);
		return ResponseEntity.ok(allReceipts);
	}
	
	@GetMapping(path = "/no_transfer_receipts", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Receipt>> allReceiptsWithoutTransfer(@AuthenticationPrincipal User user) {
		
		List<Receipt> allReceipts = receiptService.receiptsNotUsedInTransfer(user);
		return ResponseEntity.ok(allReceipts);
	}


	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Receipt> getReceiptById(@PathVariable Long id, @AuthenticationPrincipal User user) {
		
		Optional<Receipt> result = receiptService.getById(user, id);
		if (result.isPresent()) return new ResponseEntity<>(result.get(), HttpStatus.OK);
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//		return receiptService.getById(user, id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
		receiptService.deleteReceipt(user, id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Receipt>> searchReceipts(@RequestParam(defaultValue = "") String key,
			@AuthenticationPrincipal User user) {
		
		List<Receipt> allReceipts = receiptService.searchReceiptsByClientName(user, key);
		return ResponseEntity.ok(allReceipts);
	}
	
}
