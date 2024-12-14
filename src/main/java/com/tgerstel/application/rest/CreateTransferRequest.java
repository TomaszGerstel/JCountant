package com.tgerstel.application.rest;

import com.tgerstel.domain.TransferType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateTransferRequest {

    @NotNull
    private TransferType transferType;
    @NotNull(message = "enter amount value")
    private BigDecimal amount;
    private String from;
    private String to;
    @NotNull(message = "enter date")
    private LocalDate date;
    private String description;
}
