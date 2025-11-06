package main.java.com.book_recs.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for sending emails (e.g., verification codes).
 * Uses JavaMailSender configured in EmailConfiguration.
 */
@Service
public class emailService {

    private static final Logger log = LoggerFactory.getLogger(emailService.class);

    private final JavaMailSender mailSender;

    // Optional: default "from" address pulled from configuration
    @Value("${spring.mail.username:}")
    private String from;

    // Prefer constructor injection
    @Autowired
    public emailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a simple verification email with a plaintext code.
     *
     * @param toEmail recipient address
     * @param verificationCode code to include in the message
     * @throws MailException if sending fails
     */
    public void sendVerificationEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        if (from != null && !from.isBlank()) {
            message.setFrom(from);
        }
        message.setSubject("Email Verification");
        message.setText("Your verification code is: " + verificationCode);

        try {
            mailSender.send(message);
            log.info("Verification email sent to {}", toEmail);
        } catch (MailException ex) {
            log.error("Failed to send verification email to {}", toEmail, ex);
            throw ex;
        }
    }
}
