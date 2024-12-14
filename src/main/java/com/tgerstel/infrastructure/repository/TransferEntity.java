package com.tgerstel.infrastructure.repository;

import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.TransferType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transfer")
public class TransferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransferType transferType;
    private BigDecimal amount;
    @Column(name = "from_")
    private String from;
    @Column(name = "to_")
    private String to;
    private LocalDate date;
    private LocalDateTime baseDate;
    private String description;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receipt", referencedColumnName = "id")
    private ReceiptEntity receiptEntity;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_base_id", referencedColumnName = "id")
    private User user;

    @PrePersist
    public void setBaseDate() {
        this.baseDate = LocalDateTime.now();
    }

    public TransferEntity(@NotNull TransferType transferType, @NotNull BigDecimal amount, @Nullable String from,
                          @Nullable String to, @NotNull LocalDate date, @Nullable String description,
                          @Nullable ReceiptEntity receipt, @NotNull User user) {

        this.transferType = transferType;
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
        this.description = description;
        this.receiptEntity = receipt;
        this.user = user;
    }

    public Transfer toTransfer() {
        return new Transfer(id, transferType, amount, from, to, date, baseDate, description,
                receiptEntity == null ? null : receiptEntity.toReceipt(), user);
    }
}
