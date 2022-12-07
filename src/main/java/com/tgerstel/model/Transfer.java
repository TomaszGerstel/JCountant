package com.tgerstel.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private TransferType transferType;
    private Float amount;
    @Column(name="`from`")
    private String from;
    @Column(name="`to`")
    private String to;
    private LocalDateTime date;
    private LocalDateTime baseDate;
    private String description;
    @OneToOne
    @JoinColumn(name="receipt", referencedColumnName="id")
    private Receipt receipt;
    @ManyToOne
    @JoinColumn(name="`user`", referencedColumnName="id")
    private User user;

    @PrePersist
    public void setBaseDate() {
        this.baseDate = LocalDateTime.now();
    }

    public Transfer(@NotNull TransferType transferType, @NotNull Float amount, @Nullable String from,
                    @Nullable String to, @NotNull LocalDateTime date, @Nullable String description,
                    @NotNull Receipt receipt, @NotNull User user) {

        this.transferType = transferType;
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
        this.description = description;
        this.receipt = receipt;
        this.user = user;
    }
}
