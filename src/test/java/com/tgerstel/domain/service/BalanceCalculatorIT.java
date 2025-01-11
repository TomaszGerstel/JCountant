package com.tgerstel.domain.service;

import com.tgerstel.JCountantApplication;
import com.tgerstel.configuration.JCountantPostgreSqlContainer;
import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.TransferType;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import com.tgerstel.domain.service.command.CreateTransferCommand;
import com.tgerstel.infrastructure.repository.*;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = JCountantApplication.class)
@ActiveProfiles("it")
public class BalanceCalculatorIT {

   @Autowired
   private BalanceCalculator balanceCalculator;

    @Autowired
    private TransferService transferService;

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Container
    public static PostgreSQLContainer<JCountantPostgreSqlContainer> postgreSQLContainer =
            JCountantPostgreSqlContainer.getInstance();

    @Test
    @Transactional
    public void shouldReturnCurrentBalanceForUser() {
        // given
        User user1 = createUser("user2123");
        User user2 = createUser("user2234");

        var receipt1Amount = BigDecimal.valueOf(600).setScale(2);
        var receipt2Amount = BigDecimal.valueOf(500).setScale(2);
        var receipt3Amount = BigDecimal.valueOf(1000).setScale(2);

        Receipt receipt1 = createReceipt(receipt1Amount, user1);
        Receipt receipt2 = createReceipt(receipt2Amount, user2);
        Receipt receipt3 = createReceipt(receipt3Amount, user1);

        var expectedUser1BalanceAmount = receipt1Amount.add(receipt3Amount);

        // when
        createTransfer(receipt1Amount, receipt1.getId(), user1);
        createTransfer(receipt2Amount, receipt2.getId(), user2);
        createTransfer(receipt3Amount, receipt3.getId(), user1);

        // then
        var balanceResult = balanceCalculator.currentBalance(user1);

        assertThat(balanceResult.getBalance()).isEqualTo(expectedUser1BalanceAmount);
    }

    private User createUser(String username) {
        User user = new User(null, username, "test@gmail.com", "secret", 12, Set.of());
        Long id = userSpringRepository.save(new UserEntity(user)).getId();
        user.setId(id);
        return user;
    }

    private Receipt createReceipt(BigDecimal amount, User user) {
        CreateReceiptCommand command = new CreateReceiptCommand(LocalDate.now(), amount,
                BigDecimal.valueOf(800), null, null, "client_01", "worker_01", "for_example", user);
        return receiptService.createReceipt(command);
    }

    private void createTransfer(BigDecimal amount, Long receiptId, User user) {
        CreateTransferCommand command = new CreateTransferCommand(TransferType.IN_TRANSFER, amount, "company",
                "office", LocalDate.now(), null, receiptId, user);
        transferService.createTransfer(command);
    }

}
