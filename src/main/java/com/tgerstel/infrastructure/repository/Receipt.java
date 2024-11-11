package com.tgerstel.infrastructure.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	@NotNull(message = "enter amount value")
	@Positive(message = "the amount must be a positive value")
	private BigDecimal amount;
	@Positive
	private BigDecimal netAmount;
	@Positive
	private BigDecimal vatValue;
	@Positive
	private Float vatPercentage;
	@NotBlank(message = "enter client name")
	private String client;
	@NotBlank(message = "enter worker name")
	private String worker;
	@NotBlank(message = "add description")
	private String description;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_base_id", referencedColumnName = "id")
	private User user;

	public Receipt(@NotNull LocalDate date, @NotNull BigDecimal amount, @Nullable BigDecimal netAmount,
			@Nullable BigDecimal vatValue, @Nullable Float vatPercentage, @NotNull String client,
			@NotNull String worker, @NotNull String description, @Nullable User user) {

		this.date = date;
		this.amount = amount;
		this.netAmount = netAmount;
		this.vatValue = vatValue;
		this.vatPercentage = vatPercentage;
		this.client = client;
		this.worker = worker;
		this.description = description;
		this.user = user;
	}
	
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
