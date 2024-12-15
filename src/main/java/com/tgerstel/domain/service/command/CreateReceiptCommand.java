package com.tgerstel.domain.service.command;

import com.tgerstel.domain.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateReceiptCommand(LocalDate date, BigDecimal amount, BigDecimal netAmount, BigDecimal vatValue,
                                   Float vatPercentage, String client, String worker, String description, User user) {}

