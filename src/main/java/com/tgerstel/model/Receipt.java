package com.tgerstel.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @NotNull
    @Positive(message = "the amount must be a positive value")
    private Float amount;
    @Positive
    private Float netAmount;
    @Positive
    private Float vatValue;
    @Positive
    private Float vatPercentage;
    @NotBlank(message = "enter client name")
    private String client;
    @NotBlank(message = "enter worker name")
    private String worker;
    @NotBlank(message = "add description")
    private String description;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_base_id", referencedColumnName = "id")
    private User user;

    public Receipt(@NotNull LocalDate date, @NotNull Float amount, @Nullable Float netAmount,
                   @Nullable Float vatValue, @Nullable Float vatPercentage, @NotNull String client,
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

    public Float getNetAmount() {
    	if(netAmount == null) {
    		if(vatValue != null) return amount - vatValue;
    		if(vatPercentage != null) return amount / (vatPercentage / 100);
    	}
  		return netAmount;      	
      }
    
    public Float getVatValue() {
    	if(vatValue == null) {
    		if(netAmount != null) return amount - netAmount;
    		if(vatPercentage != null) return vatPercentage * (amount / 100);
    	}
  		return vatValue;
    	
    }
    
    
}
