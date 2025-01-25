package com.tgerstel.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BalanceResults {

    @Getter
    private final Float flatTaxRate = 19.0f;
    private final Integer defaultLumpTaxRate = 12;

    @Getter
    private final BigDecimal costs;
    @Getter
    private final BigDecimal grossCosts;
    @Getter
    private final BigDecimal grossIncome;
    @Getter
    private final BigDecimal netIncome;
    private final Integer lumpTaxRate;
    @Getter
    private final BigDecimal profitPaid;
    @Getter
    private final BigDecimal vatPaid;
    @Getter
    private final BigDecimal taxPaid;

    public BalanceResults(final BigDecimal costs, final BigDecimal grossCosts, final BigDecimal grossIncome,
                          final  BigDecimal netIncome, final BigDecimal profitPaid, final BigDecimal vatPaid,
                          final BigDecimal taxPaid, final Integer lumpTaxRate) {
        super();
        this.costs = costs;
        this.grossCosts = grossCosts;
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
        this.profitPaid = profitPaid;
        this.vatPaid = vatPaid;
        this.taxPaid = taxPaid;
        this.lumpTaxRate = lumpTaxRate;
    }

    public BigDecimal getNetBalance() {
        return getNetIncome().subtract(getCosts()).setScale(2, RoundingMode.HALF_EVEN);
    }

    public Integer getLumpTaxRate() {
        if (lumpTaxRate == null) return defaultLumpTaxRate;
        return lumpTaxRate;
    }

    public BigDecimal getLumpSumTaxDue() {
        return netIncome.multiply(BigDecimal.valueOf(getLumpTaxRate())).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getFlatTaxDue() {
        return getNetBalance().multiply(BigDecimal.valueOf(flatTaxRate.intValue()))
                .divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getVatBalance() {
        return getVatDue().subtract(vatPaid).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getFlatTaxBalance() {
        return getFlatTaxDue().subtract(taxPaid).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getLumpSumTaxBalance() {
        return getLumpSumTaxDue().subtract(taxPaid).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getProfitDueFlat() {
        return getNetBalance().subtract(getFlatTaxDue()).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getProfitRemainingFlat() {
        return getProfitDueFlat().subtract(profitPaid).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getProfitDueLump() {
        return getNetBalance().subtract(getLumpSumTaxDue()).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getProfitRemainingLump() {
        return getProfitDueLump().subtract(profitPaid).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getOtherCosts() {
        return vatPaid.add(taxPaid).add(profitPaid).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getBalance() {
        return grossIncome.subtract(grossCosts).subtract(getOtherCosts()).setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getVatDue() {
        return getGrossIncome().subtract(getGrossCosts()).subtract(getNetBalance()).setScale(2, RoundingMode.HALF_EVEN);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((costs == null) ? 0 : costs.hashCode());
        result = prime * result + defaultLumpTaxRate.hashCode();
        result = prime * result + flatTaxRate.hashCode();
        result = prime * result + ((netIncome == null) ? 0 : netIncome.hashCode());
        result = prime * result + ((lumpTaxRate == null) ? 0 : lumpTaxRate.hashCode());
        result = prime * result + ((profitPaid == null) ? 0 : profitPaid.hashCode());
        result = prime * result + ((taxPaid == null) ? 0 : taxPaid.hashCode());
        result = prime * result + ((vatPaid == null) ? 0 : vatPaid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BalanceResults other = (BalanceResults) obj;
        if (costs == null) {
            if (other.costs != null)
                return false;
        } else if (!costs.equals(other.costs))
            return false;
        if (!defaultLumpTaxRate.equals(other.defaultLumpTaxRate))
            return false;
        if (!flatTaxRate.equals(other.flatTaxRate))
            return false;
        if (netIncome == null) {
            if (other.netIncome != null)
                return false;
        } else if (!netIncome.equals(other.netIncome))
            return false;
        if (lumpTaxRate == null) {
            if (other.lumpTaxRate != null)
                return false;
        } else if (!lumpTaxRate.equals(other.lumpTaxRate))
            return false;
        if (profitPaid == null) {
            if (other.profitPaid != null)
                return false;
        } else if (!profitPaid.equals(other.profitPaid))
            return false;
        if (taxPaid == null) {
            if (other.taxPaid != null)
                return false;
        } else if (!taxPaid.equals(other.taxPaid))
            return false;
        if (vatPaid == null) {
            return other.vatPaid == null;
        } else return vatPaid.equals(other.vatPaid);
    }

    @Override
    public String toString() {
        return "BalanceResults [getCosts()=" + getCosts() + ", getGrossCosts()=" + getGrossCosts()
                + ", getGrossIncome()=" + getGrossIncome() + ", getNetIncome()=" + getNetIncome() + ", getProfitPaid()="
                + getProfitPaid() + ", getVatPaid()=" + getVatPaid() + ", getTaxPaid()=" + getTaxPaid()
                + ", getNetBalance()=" + getNetBalance() + ", getLumpTaxRate()=" + getLumpTaxRate()
                + ", getLumpSumTaxDue()=" + getLumpSumTaxDue() + ", getFlatTaxDue()=" + getFlatTaxDue()
                + ", getVatBalance()=" + getVatBalance() + ", getFlatTaxBalance()=" + getFlatTaxBalance()
                + ", getLumpSumTaxBalance()=" + getLumpSumTaxBalance() + ", getProfitDueFlat()=" + getProfitDueFlat()
                + ", getProfitRemainingFlat()=" + getProfitRemainingFlat() + ", getProfitDueLump()="
                + getProfitDueLump() + ", getProfitRemainingLump()=" + getProfitRemainingLump() + ", getFlatTaxRate()="
                + getFlatTaxRate() + ", getOtherCosts()=" + getOtherCosts() + ", getBalance()=" + getBalance()
                + ", getVatDue()=" + getVatDue() + ", hashCode()=" + hashCode() + "]";
    }

}
