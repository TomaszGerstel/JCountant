package com.tgerstel.calculations;

import java.math.BigDecimal;

import com.tgerstel.model.TransferType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionModel {	

	private TransferType transferType;
	private BigDecimal amount;
	private BigDecimal vatValue;
	private BigDecimal netAmount;	
	
	public TransactionModel() {}
	
}
