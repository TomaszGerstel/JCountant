package com.tgerstel.model;

import lombok.Data;

@Data
public class BalanceResults {
	
	private Float balance;
	private Float costs;
	private Float otherCosts;
	private Float grossIncome;
	private Float netBalance;
	private Float vatBalance;
	private Float flatTaxBalance;
	private Float vatDue;
	private Float flatTaxDue;
	private Float lumpTaxRate;
	private Float lumpSumTaxDue;
	private Float lumpSumTaxBalance;
	private Float profitDueFlat;
	private Float profitDueLump;
	private Float profitPaid;
	private Float vatPaid;
	private Float taxPaid;
	private Float profitRemainingFlat;
	private Float profitRemainingLump;
	
	public BalanceResults() {
		super();
	}

}
