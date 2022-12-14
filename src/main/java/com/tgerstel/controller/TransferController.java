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
import com.tgerstel.model.Transfer;
import com.tgerstel.service.ReceiptService;
import com.tgerstel.service.TransferService;

import jakarta.validation.Valid;


@RestController
@RequestMapping(path="/api/transfer", produces="application/json")
@CrossOrigin(origins="*")
public class TransferController {
	
	private final TransferService transferService;
	
	public TransferController(TransferService transferService) {
		super();
		this.transferService = transferService;
	}

	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addReceipt(@RequestBody @Valid Transfer transfer, Long receiptId, Errors errors,
			@AuthenticationPrincipal User user) {

		if (errors.hasErrors()) {
			List<String> errorMessage = errors.getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());			
			return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Transfer saved = transferService.createTransfer(transfer, receiptId, user).get();
		URI savedTransferLocation = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(saved.getId())
				.toUri();
		
		return ResponseEntity.created(savedTransferLocation).body(saved);
	}
	
	@GetMapping(path = "/recent", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Transfer>> allTranfsers(@RequestParam(defaultValue = "10") Integer resultSize,
			@AuthenticationPrincipal User user) {
		
		List<Transfer> allTransfers = transferService.getRecentTransfers(user, resultSize);
		return ResponseEntity.ok(allTransfers);
	}

	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Transfer> getTransferById(@PathVariable Long id, @AuthenticationPrincipal User user) {
		
		Optional<Transfer> result = transferService.getById(user, id);
		if (result.isPresent()) return new ResponseEntity<>(result.get(), HttpStatus.OK);
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
		transferService.deleteTransfer(user, id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Transfer>> searchTransfers(@RequestParam(defaultValue = "") String key,
			@AuthenticationPrincipal User user) {
		
		List<Transfer> allTransfers = transferService.searchTransfersByFromName(user, key);
		return ResponseEntity.ok(allTransfers);
	}
	
}
