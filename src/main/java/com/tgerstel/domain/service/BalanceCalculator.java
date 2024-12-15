package com.tgerstel.domain.service;

import com.tgerstel.domain.BalanceResults;
import com.tgerstel.domain.User;

import java.time.LocalDate;

public interface BalanceCalculator {

    BalanceResults currentBalance(User user);

    BalanceResults balanceToCurrentMonth(User user);

    BalanceResults balanceToLastMonth(User user);

    BalanceResults balanceToDateRange(LocalDate from, LocalDate to, User user);
}
