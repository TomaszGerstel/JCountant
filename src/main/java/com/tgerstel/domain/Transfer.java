package com.tgerstel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
public class Transfer {

    @Getter
    private final Long id;
    @Getter
    private final TransferType transferType;
    private final BigDecimal amount;
    @Getter
    @Setter
    private String from;
    @Getter
    @Setter
    private String to;
    @Getter
    @Setter
    private LocalDate date;
    @Getter
    private final LocalDateTime baseDate;
    @Getter
    @Setter
    private String description;
    @Getter
    private final Receipt receipt;
    @Getter
    private final User user;

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
