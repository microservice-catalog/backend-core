package ru.stepagin.dockins.external.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.stepagin.dockins.domain.auth.entity.AccountEntity;
import ru.stepagin.dockins.domain.auth.entity.EmailConfirmationEntity;
import ru.stepagin.dockins.domain.auth.repository.EmailConfirmationRepository;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private static final int EXPIRATION_MINUTES = 15;
    private final EmailConfirmationRepository emailConfirmationRepository;
    private final JavaMailSender mailSender;

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

        sendEmail(account.getEmail(), code);
    }

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
        // Можно через отдельный AccountRepository.save(account), но предполагаем, что каскадно или через сервис сохранится
    }

    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Код подтверждения регистрации");
        message.setText("Ваш код подтверждения: " + code);
        mailSender.send(message);
    }

    private String generateConfirmationCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 6-значное число
        return String.valueOf(number);
    }
}

