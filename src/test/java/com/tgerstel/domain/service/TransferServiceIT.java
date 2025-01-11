package com.tgerstel.domain.service;

import com.tgerstel.JCountantApplication;
import com.tgerstel.configuration.JCountantPostgreSqlContainer;
import com.tgerstel.domain.Transfer;
import com.tgerstel.domain.TransferType;
import com.tgerstel.domain.User;
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
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(classes = JCountantApplication.class)
@ActiveProfiles("it")
public class TransferServiceIT {

    @Autowired
    private TransferService transferService;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Autowired
    private TransferSpringRepository transferSpringRepository;

    @Container
    public static PostgreSQLContainer<JCountantPostgreSqlContainer> postgreSQLContainer =
            JCountantPostgreSqlContainer.getInstance();

    @Test
    @Transactional
    public void shouldCreateTransferForUser() {
        // given
        User user = createUser("user1123");
        BigDecimal amount = BigDecimal.valueOf(920).setScale(2);

        CreateTransferCommand createTransferCommand = new CreateTransferCommand(TransferType.SALARY, amount,
                "company", "worker_1", LocalDate.now(), null, null, user);

        // when
        transferService.createTransfer(createTransferCommand);

        // then
        List<TransferEntity> transfers = transferSpringRepository.findAllByUserId(user.getId());
        TransferEntity transfer = transfers.get(0);

        assertThat(transfers).size().isEqualTo(1);
        assertThat(transfer.getUser().getId()).isEqualTo(user.getId());
        assertThat(transfer.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(transfer.getAmount()).isEqualTo(amount);
    }

    @Test
    @Transactional
    public void shouldReturnExistingTransfersForUser() {
        // given
        var transfer1Amount = BigDecimal.valueOf(920).setScale(2);
        var transfer2Amount = BigDecimal.valueOf(500).setScale(2);
        var transfer3Amount = BigDecimal.valueOf(1000).setScale(2);

        User user = createUser("user121234");
        User user2 = createUser("user122432");

        createTransfer(transfer1Amount, user);
        createTransfer(transfer2Amount, user);
        createTransfer(transfer3Amount, user2);

        // when
        List<Transfer> receipts = transferService.getRecentTransfers(user, 10);

        // then
        assertThat(receipts).size().isEqualTo(2);
        for (Transfer transfer : receipts) {
            assertThat(transfer.getUser().getId()).isEqualTo(user.getId());
            assertThat(transfer.getUser().getUsername()).isEqualTo(user.getUsername());
            assertThat(List.of(transfer1Amount, transfer2Amount)).contains(transfer.getAmount());
        }
    }

    private User createUser(String username) {
        User user = new User(null, username, "test@gmail.com", "secret", 12, Set.of());
        Long id = userSpringRepository.save(new UserEntity(user)).getId();
        user.setId(id);
        return user;
    }

    private void createTransfer(BigDecimal amount, User user) {
        CreateTransferCommand command = new CreateTransferCommand(TransferType.TAX_OUT_TRANSFER, amount, "company",
                "office", LocalDate.now(), null, null, user);
        transferService.createTransfer(command);
    }

}
