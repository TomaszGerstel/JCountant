package com.tgerstel.infrastructure.configuration;

import com.tgerstel.domain.repository.ReceiptRepository;
import com.tgerstel.domain.repository.TransferRepository;
import com.tgerstel.domain.repository.UserRepository;
import com.tgerstel.domain.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ComponentConfiguration {

    @Bean
    BalanceCalculator balanceCalculator(final TransferRepository transferRepository) {
        return new BalanceCalculationService(transferRepository);
    }

    @Bean
    ReceiptService receiptService(final ReceiptRepository receiptRepository, final TransferRepository transferRepository) {
        return new DomainReceiptService(receiptRepository, transferRepository);
    }

    @Bean
    TransferService transferService(final TransferRepository transferRepository, final ReceiptRepository receiptRepository) {
        return new DomainTransferService(transferRepository, receiptRepository);
    }

    @Bean
    UserService userService(final UserRepository userRepository) {
        return new DomainUserService(userRepository);
    }
}
