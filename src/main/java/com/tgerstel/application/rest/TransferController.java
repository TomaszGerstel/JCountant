package com.tgerstel.application.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.TransferService;
import com.tgerstel.domain.service.command.CreateTransferCommand;
import lombok.AllArgsConstructor;
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

import com.tgerstel.domain.TransferType;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@OpenAPIDefinition(info = @Info(title = "Receipt API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
@RequestMapping(path = "/api/transfer", produces = "application/json")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private static final List<TransferType> TRANSFER_REQUIRED_RECEIPT =
            List.of(TransferType.IN_TRANSFER, TransferType.OUT_TRANSFER);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTransfer(@RequestBody @Valid final CreateTransferRequest request, final Errors errors,
                                         final Long receiptId, @AuthenticationPrincipal final User user) {

        if (receiptId == null && TRANSFER_REQUIRED_RECEIPT.contains(request.getTransferType()))
            errors.rejectValue("transferType", "422", request.getTransferType() + " must include a receipt");

        if (errors.hasErrors()) {
            final List<String> errorMessage = errors.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());
            return new ResponseEntity<>(errorMessage, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        final CreateTransferCommand command = new CreateTransferCommand(request.getTransferType(), request.getAmount(),
                request.getFrom(), request.getTo(), request.getDate(), request.getDescription(), receiptId, user);

        final Optional<Transfer> saved = transferService.createTransfer(command);

        if (saved.isEmpty())
            return new ResponseEntity<>(List.of("selected receipt is unavailable"), HttpStatus.UNPROCESSABLE_ENTITY);

        final URI savedTransferLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.get().getId()).toUri();

        return ResponseEntity.created(savedTransferLocation).body(saved);
    }

    @GetMapping(path = "/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transfer>> allTransfers(@RequestParam(defaultValue = "10") final Integer resultSize,
                                                       @AuthenticationPrincipal final User user) {

        final List<Transfer> allTransfers = transferService.getRecentTransfers(user, resultSize);
        return ResponseEntity.ok(allTransfers);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transfer> getTransferById(@PathVariable final Long id, @AuthenticationPrincipal final User user) {

        final Optional<Transfer> result = transferService.getById(user, id);
        return result.map(transfer -> new ResponseEntity<>(transfer, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final Long id, @AuthenticationPrincipal final User user) {
        transferService.deleteTransfer(user, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transfer>> searchTransfers(@RequestParam(defaultValue = "") final String key,
                                                          @AuthenticationPrincipal final User user) {
        final List<Transfer> allTransfers = transferService.searchTransfersForUserWithSenderData(user, key);
        return ResponseEntity.ok(allTransfers);
    }

}
