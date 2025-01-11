package com.tgerstel.domain;

import java.math.BigDecimal;

public class BalanceResults {

    private final Float flatTaxRate = 19.0f;
    private Integer defaultLumpTaxRate = 12;

    private BigDecimal costs;
    private BigDecimal grossCosts;
    private BigDecimal grossIncome;
    private BigDecimal netIncome;
    private Integer lumpTaxRate;
    private BigDecimal profitPaid;
    private BigDecimal vatPaid;
    private BigDecimal taxPaid;

    public BalanceResults(BigDecimal costs, BigDecimal grossCosts, BigDecimal grossIncome, BigDecimal netIncome,
                          BigDecimal profitPaid, BigDecimal vatPaid, BigDecimal taxPaid, Integer lumpTaxRate) {
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

    public BigDecimal getCosts() {
        return costs;
    }

    public BigDecimal getGrossCosts() {
        return grossCosts;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public BigDecimal getProfitPaid() {
        return profitPaid;
    }

    public BigDecimal getVatPaid() {
        return vatPaid;
    }

    public BigDecimal getTaxPaid() {
        return taxPaid;
    }

    public BigDecimal getNetBalance() {
        return getNetIncome().subtract(getCosts()).setScale(2);
    }

    public Integer getLumpTaxRate() {
        if (lumpTaxRate == null) return defaultLumpTaxRate;
        return lumpTaxRate;
    }

    public BigDecimal getLumpSumTaxDue() {
        return netIncome.multiply(BigDecimal.valueOf(getLumpTaxRate())).divide(BigDecimal.valueOf(100)).setScale(2);
    }

    public BigDecimal getFlatTaxDue() {
        return getNetBalance().multiply(BigDecimal.valueOf(flatTaxRate.intValue()))
                .divide(BigDecimal.valueOf(100)).setScale(2);
    }

    public BigDecimal getVatBalance() {
        return getVatDue().subtract(vatPaid).setScale(2);
    }

    public BigDecimal getFlatTaxBalance() {
        return getFlatTaxDue().subtract(taxPaid).setScale(2);
    }

    public BigDecimal getLumpSumTaxBalance() {
        return getLumpSumTaxDue().subtract(taxPaid).setScale(2);
    }

    public BigDecimal getProfitDueFlat() {
        return getNetBalance().subtract(getFlatTaxDue()).setScale(2);
    }

    public BigDecimal getProfitRemainingFlat() {
        return getProfitDueFlat().subtract(profitPaid).setScale(2);
    }

    public BigDecimal getProfitDueLump() {
        return getNetBalance().subtract(getLumpSumTaxDue()).setScale(2);
    }

    public BigDecimal getProfitRemainingLump() {
        return getProfitDueLump().subtract(profitPaid).setScale(2);
    }

    public Float getFlatTaxRate() {
        return flatTaxRate;
    }

    public BigDecimal getOtherCosts() {
        return vatPaid.add(taxPaid).add(profitPaid).setScale(2);
    }

    public BigDecimal getBalance() {
        return grossIncome.subtract(grossCosts).subtract(getOtherCosts()).setScale(2);
    }

    public BigDecimal getVatDue() {
        return getGrossIncome().subtract(getGrossCosts()).subtract(getNetBalance()).setScale(2);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((costs == null) ? 0 : costs.hashCode());
        result = prime * result + ((defaultLumpTaxRate == null) ? 0 : defaultLumpTaxRate.hashCode());
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
        if (defaultLumpTaxRate == null) {
            if (other.defaultLumpTaxRate != null)
                return false;
        } else if (!defaultLumpTaxRate.equals(other.defaultLumpTaxRate))
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
