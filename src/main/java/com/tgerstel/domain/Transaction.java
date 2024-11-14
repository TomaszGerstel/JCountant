package com.tgerstel.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {

	private TransferType transferType;
	private BigDecimal amount;
	private BigDecimal vatValue;
	private BigDecimal netAmount;	
	
	public Transaction() {}
	
}
