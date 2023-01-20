package com.tgerstel.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TransferType transferType;
    @NotNull(message = "enter amount value")
    private BigDecimal amount;
    @Column(name="from_")
    private String from;
    @Column(name="to_")
    private String to;
    @NotNull(message = "enter date")
    private LocalDate date;
    private LocalDateTime baseDate;
    private String description;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="receipt", referencedColumnName="id")
    private Receipt receipt;
    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_base_id", referencedColumnName="id")
    private User user;

    @PrePersist
    public void setBaseDate() {
        this.baseDate = LocalDateTime.now();
    }

    public Transfer(@NotNull TransferType transferType, @NotNull BigDecimal amount, @Nullable String from,
                    @Nullable String to, @NotNull LocalDate date, @Nullable String description,
                    @Nullable Receipt receipt, @NotNull User user) {

        this.transferType = transferType;
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
        this.description = description;
        this.receipt = receipt;
        this.user = user;
    }
    
    public BigDecimal getAmount() {
    	return amount.setScale(2, RoundingMode.HALF_EVEN);
    }
}
