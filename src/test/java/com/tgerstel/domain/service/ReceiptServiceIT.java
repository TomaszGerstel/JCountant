package com.tgerstel.domain.service;

import com.tgerstel.JCountantApplication;
import com.tgerstel.configuration.JCountantPostgreSqlContainer;
import com.tgerstel.domain.Receipt;
import com.tgerstel.domain.User;
import com.tgerstel.domain.service.command.CreateReceiptCommand;
import com.tgerstel.infrastructure.repository.ReceiptEntity;
import com.tgerstel.infrastructure.repository.ReceiptSpringRepository;
import com.tgerstel.infrastructure.repository.UserEntity;
import com.tgerstel.infrastructure.repository.UserSpringRepository;
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
public class ReceiptServiceIT {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private UserSpringRepository userSpringRepository;

    @Autowired
    private ReceiptSpringRepository receiptSpringRepository;

    @Container
    public static PostgreSQLContainer<JCountantPostgreSqlContainer> postgreSQLContainer =
            JCountantPostgreSqlContainer.getInstance();

    @Test
    @Transactional
    public void shouldCreateReceiptForUser() {
        // given
        User user = createUser("user0123");
        BigDecimal amount = BigDecimal.valueOf(920).setScale(2);
        CreateReceiptCommand command = new CreateReceiptCommand(LocalDate.now(), amount,
                BigDecimal.valueOf(800), null, null, "client_01", "worker_01", "for_example", user);

        // when
        receiptService.createReceipt(command);

        // then
        List<ReceiptEntity> receipts = receiptSpringRepository.findAllByUserId(user.getId());
        ReceiptEntity receipt = receipts.get(0);

        assertThat(receipts).size().isEqualTo(1);
        assertThat(receipt.getUser().getId()).isEqualTo(user.getId());
        assertThat(receipt.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(receipt.getAmount()).isEqualTo(amount);
    }

    @Test
    @Transactional
    public void shouldReturnExistingReceiptsForUser() {
        // given
        var receipt1Amount = BigDecimal.valueOf(920).setScale(2);
        var receipt2Amount = BigDecimal.valueOf(500).setScale(2);
        var receipt3Amount = BigDecimal.valueOf(1000).setScale(2);

        User user = createUser("user021234");
        User user2 = createUser("user022432");

        createReceipt(receipt1Amount, user);
        createReceipt(receipt2Amount, user);
        createReceipt(receipt3Amount, user2);

        // when
        List<Receipt> receipts = receiptService.getRecentReceipts(user, 10);

        // then
        assertThat(receipts).size().isEqualTo(2);
        for (Receipt receipt : receipts) {
            assertThat(receipt.getUser().getId()).isEqualTo(user.getId());
            assertThat(receipt.getUser().getUsername()).isEqualTo(user.getUsername());
            assertThat(List.of(receipt1Amount, receipt2Amount)).contains(receipt.getAmount());
        }
    }

    private User createUser(String username) {
        User user = new User(null, username, "natan@gmail.com", "secret", 12, Set.of());
        Long id = userSpringRepository.save(new UserEntity(user)).getId();
        user.setId(id);
        return user;
    }

    private void createReceipt(BigDecimal amount, User user) {
        CreateReceiptCommand command = new CreateReceiptCommand(LocalDate.now(), amount,
                BigDecimal.valueOf(800), null, null, "client_01", "worker_01", "for_example", user);
        receiptService.createReceipt(command);
    }

}
