package com.tgerstel.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tgerstel.domain.Receipt;
import jakarta.persistence.*;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "receipt")
@NoArgsConstructor
public class ReceiptEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate date;
	private BigDecimal amount;
	private BigDecimal netAmount;
	private BigDecimal vatValue;
	private Float vatPercentage;
	private String client;
	private String worker;
	private String description;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_base_id", referencedColumnName = "id")
	private UserEntity user;

	public ReceiptEntity(@NotNull LocalDate date, @NotNull BigDecimal amount, @Nullable BigDecimal netAmount,
						 @Nullable BigDecimal vatValue, @Nullable Float vatPercentage, @NotNull String client,
						 @NotNull String worker, @NotNull String description, @Nullable UserEntity user) {

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

	public Receipt toReceipt() {
		return new Receipt(id, date, amount, netAmount, vatValue, vatPercentage, client, worker, description, user.toUser());
	}

}
