package com.tgerstel.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.tgerstel.domain.*;
import com.tgerstel.domain.repository.TransferRepository;

public class BalanceCalculationService implements BalanceCalculator {

    private final TransferRepository transferRepository;

    public BalanceCalculationService(final TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public BalanceResults currentBalance(final User user) {
        return calculateBalance(transferRepository.getAllForUser(user), user);
    }

    public BalanceResults balanceToCurrentMonth(final User user) {
        return balanceForMonth(YearMonth.now(), user);
    }

    public BalanceResults balanceToLastMonth(final User user) {
        return balanceForMonth(YearMonth.now().minusMonths(1), user);
    }

    public BalanceResults balanceToDateRange(final LocalDate from, final LocalDate to, final User user) {
        final List<Transfer> transfers = transferRepository.getForUserInRange(from.minusDays(1),
                to.plusDays(1), user);
        return calculateBalance(transfers, user);
    }

    private BalanceResults balanceForMonth(final YearMonth month, final User user) {
        final LocalDate from = month.atDay(1);
        final LocalDate to = month.atEndOfMonth();
        return balanceToDateRange(from, to, user);
    }

    private BalanceResults calculateBalance(final List<Transfer> transfers, final User user) {
        List<Transaction> transactions = createTransactions(transfers);

        final Predicate<Transaction> IS_OUT_TRANSFER = t -> t.getTransferType() == TransferType.OUT_TRANSFER;
        final Predicate<Transaction> IS_IN_TRANSFER = t -> t.getTransferType() == TransferType.IN_TRANSFER;

        return new BalanceResults(
                calculateSum(transactions, IS_OUT_TRANSFER, true),  // Costs (Net)
                calculateSum(transactions, IS_OUT_TRANSFER, false), // Costs (Gross)
                calculateSum(transactions, IS_IN_TRANSFER, false),  // Income (Gross)
                calculateSum(transactions, IS_IN_TRANSFER, true),   // Income (Net)
                calculateSumByType(transactions, TransferType.SALARY),
                calculateSumByType(transactions, TransferType.VAT_OUT_TRANSFER),
                calculateSumByType(transactions, TransferType.TAX_OUT_TRANSFER),
                user.getLumpSumTaxRate()
        );
    }

    private List<Transaction> createTransactions(final List<Transfer> transfers) {
        final List<Transaction> transactions = new ArrayList<>();
        for (Transfer transfer : transfers) {
            transactions.add(createTransaction(transfer));
        }
        return transactions;
    }

    private Transaction createTransaction(final Transfer transfer) {
        Transaction transaction = new Transaction();

        if (transfer.getReceipt() != null) {
            transaction = makeTransactionFromReceipt(transfer.getReceipt());
        } else if (transferTypeWithoutReceipt(transfer)) {
            transaction.setAmount(transfer.getAmount());
        }

        transaction.setTransferType(transfer.getTransferType());
        return transaction;
    }

    private Transaction makeTransactionFromReceipt(final Receipt receipt) {
        final Transaction transaction = new Transaction();
        transaction.setAmount(receipt.getAmount());
        transaction.setNetAmount(receipt.getNetAmount());
        transaction.setVatValue(receipt.getVatValue());
        return transaction;
    }

    private boolean transferTypeWithoutReceipt(final Transfer transfer) {
        final TransferType t = transfer.getTransferType();
        return (t == TransferType.SALARY || t == TransferType.TAX_OUT_TRANSFER || t == TransferType.VAT_OUT_TRANSFER);
    }

    private BigDecimal calculateSum(final List<Transaction> transactions, final Predicate<Transaction> predicate,
                                    final boolean netCalculation) {
        return transactions.stream()
                .filter(predicate)
                .map(t -> netCalculation && t.getNetAmount() != null ? t.getNetAmount() : t.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateSumByType(final List<Transaction> transactions, final TransferType type) {
        return transactions.stream()
                .filter(t -> t.getTransferType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
}
