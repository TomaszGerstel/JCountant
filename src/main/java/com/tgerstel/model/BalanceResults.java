package com.tgerstel.model;

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
