package ru.stepagin.dockins.core.external.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    @Value(value = "${spring.mail.username}")
    private String from;
    @Value(value = "${app.messages.mail.enabled}")
    private boolean wholeMailEnabled;

    public void sendEmail(String toEmail, String subject, String body) {
        if (!wholeMailEnabled) {
            // todo продумать бизнес логику, возможно пробросить ошибку
            log.warn("MAIL SENDING GLOBALLY DISABLED");
            log.info("Sending email to {}. Body: \n{}\n", toEmail, body);
        } else
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(toEmail);
                helper.setFrom(from);
                helper.setSubject(subject);
                helper.setText(body, true);
                mailSender.send(message);
                log.info("Email sent to {}...{} successfully", toEmail.substring(0, 4), toEmail.substring(toEmail.length() - 5));
            } catch (MessagingException e) {
                throw new RuntimeException(e.getMessage());
            }
    }
}