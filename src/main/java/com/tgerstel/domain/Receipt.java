package com.tgerstel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@AllArgsConstructor
public class Receipt {

    @Getter
    private Long id;
    @Getter
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal netAmount;
    private BigDecimal vatValue;
    @Getter
    private Float vatPercentage;
    @Getter
    private String client;
    @Getter
    private String worker;
    @Getter
    private String description;
    @Getter
    private User user;

    public BigDecimal getAmount() {
        return amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getNetAmount() {
        if (netAmount == null) {
            if (vatValue != null)
                return amount.subtract(vatValue).setScale(2, RoundingMode.HALF_EVEN);
            if (vatPercentage != null)
                return amount.divide(BigDecimal.valueOf(100 + vatPercentage), 6, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_EVEN);
            else
                return null;
        }
        return netAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getVatValue() {
        if (vatValue == null) {
            if (netAmount != null)
                return amount.subtract(netAmount).setScale(2, RoundingMode.HALF_EVEN);
            if (vatPercentage != null)
                return amount.divide(BigDecimal.valueOf(100 + vatPercentage), 6, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal.valueOf(vatPercentage)).setScale(2, RoundingMode.HALF_EVEN);
            else
                return null;
        }
        return vatValue.setScale(2, RoundingMode.HALF_EVEN);

    }


}
