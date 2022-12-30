package com.tgerstel.calculations;

public class BalanceResults {	
	
	private final Float flatTaxRate = 19.0f;
	private Integer defaultLumpTaxRate = 12;

	private Float costs;
	private Float grossIncome;
	private Float netBalance;
	private Float vatDue;
	private Integer lumpTaxRate;	
	private Float profitPaid;
	private Float vatPaid;
	private Float taxPaid;
	
	public BalanceResults(Float costs, Float grossIncome, Float netBalance, Float vatDue,
			Float profitPaid, Float vatPaid, Float taxPaid, Integer lumpTaxRate) {
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
	
	public Float getCosts() {
		return costs;
	}
	
	public Float getGrossIncome() {
		return grossIncome;
	}
	
	public Float getNetBalance() {
		return netBalance;
	}
	
	public Float getVatDue() {
		return vatDue;
	}
	
	public Float getProfitPaid() {
		return profitPaid;
	}
	
	public Float getVatPaid() {
		return vatPaid;
	}	

	public Float getTaxPaid() {
		return taxPaid;
	}
	
	public Integer getLumpTaxRate() {
		if(lumpTaxRate == null) return defaultLumpTaxRate;
		return lumpTaxRate;
	}
	
	public Float getLumpSumTaxDue() {
		return grossIncome * getLumpTaxRate() / 100;
	}
	
	public Float getFlatTaxDue() {
		return netBalance * flatTaxRate / 100;
	}
	
	public Float getVatBalance() {
		return vatDue - vatPaid;
	}
	
	public Float getFlatTaxBalance() {
		return getFlatTaxDue() - taxPaid;
	}
	
	public Float getLumpSumTaxBalance() {
		return getLumpSumTaxDue() - taxPaid;
	}

	public Float getProfitDueFlat() {
		return netBalance - getFlatTaxDue();
	}
	
	public Float getProfitRemainingFlat() {
		return getProfitDueFlat() - profitPaid; 
	}

	public Float getProfitDueLump() {
		return netBalance - getLumpSumTaxDue();
	}	

	public Float getProfitRemainingLump() {
		return getProfitDueLump() - profitPaid; 
	}

	public Float getFlatTaxRate() {
		return flatTaxRate;
	}
	
	public Float getOtherCosts() {
		return vatPaid + taxPaid + profitPaid;
	}	
		
	public Float getBalance() {
		return grossIncome + vatDue - costs - getOtherCosts();
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
