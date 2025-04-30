package ru.stepagin.dockins.core.external.mail;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stepagin.dockins.core.auth.entity.EmailConfirmationEntity;
import ru.stepagin.dockins.core.auth.repository.EmailConfirmationRepository;
import ru.stepagin.dockins.core.user.entity.AccountEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private static final int EXPIRATION_MINUTES = 15;
    private final EmailConfirmationRepository emailConfirmationRepository;
    private final EmailService emailService;
    private String confirmationTemplate;
    @Value("${app.messages.mail.confirm.enabled}")
    private boolean mailConfirmationEnabled;

    @PostConstruct
    public void init() throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("templates/confirmation_template.html");
        if (inputStream == null) {
            throw new IOException("Шаблон confirmation_template не найден");
        }
        confirmationTemplate = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }

    @Transactional
    public void sendConfirmationEmail(AccountEntity account) {
        String code = generateConfirmationCode();

        EmailConfirmationEntity confirmation = EmailConfirmationEntity.builder()
                .account(account)
                .code(code)
                .expirationTime(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES))
                .used(false)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        emailConfirmationRepository.save(confirmation);
        // todo при ошибке отправки сохранить ошибку в БД и повторить позже
        if (mailConfirmationEnabled)
            sendEmail(account.getEmail(), code);
        else {
            log.warn("MAIL CONFIRMATION DISABLED");
            log.info("Confirmation code: {}", confirmation.getCode());
        }
    }

    @Transactional
    public void confirmEmail(String email, String code) {
        EmailConfirmationEntity confirmation = emailConfirmationRepository.findTopByAccountEmailOrderByCreatedOnDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("Код подтверждения не найден"));

        if (confirmation.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Код подтверждения истёк");
        }
        if (confirmation.getUsed()) {
            throw new IllegalArgumentException("Код уже использован");
        }
        if (!confirmation.getCode().equals(code)) {
            throw new IllegalArgumentException("Неверный код подтверждения");
        }

        confirmation.markAsUsed();
        emailConfirmationRepository.save(confirmation);

        AccountEntity account = confirmation.getAccount();
        account.setEmailConfirmed(true);
        // account сохранится благодаря транзакции
    }

    private void sendEmail(String to, String code) {
        String body = confirmationTemplate.replace("{{code}}", code);
        String subject = "Код подтверждения регистрации";
        try {
            emailService.sendEmail(to, subject, body);
        } catch (MailException e) {
            throw new EmailSendingException(e.getMessage());
        }
    }

    private String generateConfirmationCode() {
//        Random random = new Random();
//        int number = random.nextInt(900000) + 100000; // 6-значное число
//        return String.valueOf(number);
        return "000000";
    }
}

