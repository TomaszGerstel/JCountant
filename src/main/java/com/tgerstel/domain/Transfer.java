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
    private Long id;
    @Getter
    private TransferType transferType;
    private BigDecimal amount;
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
    private LocalDateTime baseDate;
    @Getter
    @Setter
    private String description;
    @Getter
    private Receipt receipt;
    @Getter
    private User user;

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
