package com.tgerstel.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionModel {	

	private TransferType transferType;
	private Float amount;
	private Float vatValue;
	private Float netAmount;
	private Float vatPercentage;
	private Float taxPercentage;
	
	public TransactionModel() {}
	
}
