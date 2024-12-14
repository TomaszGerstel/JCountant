package com.tgerstel.application.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateReceiptRequest {

    @NotNull
    private LocalDate date;
    @NotNull(message = "enter amount value")
    @Positive(message = "the amount must be a positive value")
    private BigDecimal amount;
    @Positive
    private BigDecimal netAmount;
    @Positive
    private BigDecimal vatValue;
    @Positive
    private Float vatPercentage;
    @NotBlank(message = "enter client name")
    private String client;
    @NotBlank(message = "enter worker name")
    private String worker;
    @NotBlank(message = "add description")
    private String description;
}
