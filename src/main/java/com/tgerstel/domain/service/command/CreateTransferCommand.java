package com.tgerstel.domain.service.command;

import com.tgerstel.domain.TransferType;
import com.tgerstel.infrastructure.repository.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateTransferCommand(TransferType transferType, BigDecimal amount, String from, String to,
                                    LocalDate date, String description, Long receiptId, User user) {}
