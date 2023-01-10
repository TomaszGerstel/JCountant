package com.tgerstel.calculations;

import java.math.BigDecimal;

public class BalanceResults {	
	
	private final Float flatTaxRate = 19.0f;
	private Integer defaultLumpTaxRate = 12;

	private BigDecimal costs;
	private BigDecimal grossIncome;
	private BigDecimal netBalance;
	private BigDecimal vatDue;
	private Integer lumpTaxRate;	
	private BigDecimal profitPaid;
	private BigDecimal vatPaid;
	private BigDecimal taxPaid;
	
	public BalanceResults(BigDecimal costs, BigDecimal grossIncome, BigDecimal netBalance, BigDecimal vatDue,
			BigDecimal profitPaid, BigDecimal vatPaid, BigDecimal taxPaid, Integer lumpTaxRate) {
		super();
		this.costs = costs;	
		this.grossIncome = grossIncome;
		this.netBalance = netBalance;
		this.vatDue = vatDue;
		this.profitPaid = profitPaid;
		this.vatPaid = vatPaid;
		this.taxPaid = taxPaid;
		this.lumpTaxRate = lumpTaxRate;
	}
	
	public BigDecimal getCosts() {
		return costs;
	}
	
	public BigDecimal getGrossIncome() {
		return grossIncome;
	}
	
	public BigDecimal getNetBalance() {
		return netBalance;
	}
	
	public BigDecimal getVatDue() {
		return vatDue;
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
	
	public Integer getLumpTaxRate() {
		if(lumpTaxRate == null) return defaultLumpTaxRate;
		return lumpTaxRate;
	}
	
	public BigDecimal getLumpSumTaxDue() {
		return grossIncome.multiply(BigDecimal.valueOf(getLumpTaxRate())).divide(BigDecimal.valueOf(100));
	}
	
	public BigDecimal getFlatTaxDue() {
		return netBalance.multiply(BigDecimal.valueOf(flatTaxRate)).divide(BigDecimal.valueOf(100));
	}
	
	public BigDecimal getVatBalance() {
		return vatDue.subtract(vatPaid);
	}
	
	public BigDecimal getFlatTaxBalance() {
		return getFlatTaxDue().subtract(taxPaid);
	}
	
	public BigDecimal getLumpSumTaxBalance() {
		return getLumpSumTaxDue().subtract(taxPaid);
	}

	public BigDecimal getProfitDueFlat() {
		return netBalance.subtract(getFlatTaxDue());
	}
	
	public BigDecimal getProfitRemainingFlat() {
		return getProfitDueFlat().subtract(profitPaid); 
	}

	public BigDecimal getProfitDueLump() {
		return netBalance.subtract(getLumpSumTaxDue());
	}	

	public BigDecimal getProfitRemainingLump() {
		return getProfitDueLump().subtract(profitPaid); 
	}

	public Float getFlatTaxRate() {
		return flatTaxRate;
	}
	
	public BigDecimal getOtherCosts() {
		return vatPaid.add(taxPaid).add(profitPaid);
	}	
		
	public BigDecimal getBalance() {
		return grossIncome.add(vatDue).subtract(costs).subtract(getOtherCosts());
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((costs == null) ? 0 : costs.hashCode());
		result = prime * result + ((defaultLumpTaxRate == null) ? 0 : defaultLumpTaxRate.hashCode());
		result = prime * result + ((flatTaxRate == null) ? 0 : flatTaxRate.hashCode());
		result = prime * result + ((grossIncome == null) ? 0 : grossIncome.hashCode());
		result = prime * result + ((lumpTaxRate == null) ? 0 : lumpTaxRate.hashCode());
		result = prime * result + ((netBalance == null) ? 0 : netBalance.hashCode());
		result = prime * result + ((profitPaid == null) ? 0 : profitPaid.hashCode());
		result = prime * result + ((taxPaid == null) ? 0 : taxPaid.hashCode());
		result = prime * result + ((vatDue == null) ? 0 : vatDue.hashCode());
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
		if (flatTaxRate == null) {
			if (other.flatTaxRate != null)
				return false;
		} else 
			if (!flatTaxRate.equals(other.flatTaxRate))
			return false;
		if (grossIncome == null) {
			if (other.grossIncome != null)
				return false;
		} else if (!grossIncome.equals(other.grossIncome))
			return false;
		if (lumpTaxRate == null) {
			if (other.lumpTaxRate != null)
				return false;
		} else if (!lumpTaxRate.equals(other.lumpTaxRate))
			return false;
		if (netBalance == null) {
			if (other.netBalance != null)
				return false;
		} else if (!netBalance.equals(other.netBalance))
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
		if (vatDue == null) {
			if (other.vatDue != null)
				return false;
		} else if (!vatDue.equals(other.vatDue))
			return false;
		if (vatPaid == null) {
			if (other.vatPaid != null)
				return false;
		} else if (!vatPaid.equals(other.vatPaid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BalanceResults [flatTaxRate=" + flatTaxRate + ", defaultLumpTaxRate=" + defaultLumpTaxRate + ", costs="
				+ costs + ", grossIncome=" + grossIncome + ", netBalance=" + netBalance + ", vatDue=" + vatDue
				+ ", profitPaid=" + profitPaid + ", vatPaid=" + vatPaid + ", taxPaid=" + taxPaid + ", getLumpTaxRate()="
				+ getLumpTaxRate() + ", getLumpSumTaxDue()=" + getLumpSumTaxDue() + ", getFlatTaxDue()="
				+ getFlatTaxDue() + ", getVatBalance()=" + getVatBalance() + ", getFlatTaxBalance()="
				+ getFlatTaxBalance() + ", getLumpSumTaxBalance()=" + getLumpSumTaxBalance() + ", getProfitDueFlat()="
				+ getProfitDueFlat() + ", getProfitRemainingFlat()=" + getProfitRemainingFlat()
				+ ", getProfitDueLump()=" + getProfitDueLump() + ", getProfitRemainingLump()="
				+ getProfitRemainingLump() + ", getFlatTaxRate()=" + getFlatTaxRate() + ", getOtherCosts()="
				+ getOtherCosts() + ", getBalance()=" + getBalance() + "]";
	}		
	
}
