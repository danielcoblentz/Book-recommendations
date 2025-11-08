package main.java.com.book_recs.demo.service;

import com.book_recs.demo.dto.LoginUserDto;
import com.book_recs.demo.dto.RegisterUserDto;
import com.book_recs.demo.dto.VerifyUserDto;
import com.book_recs.demo.model.User;
import com.book_recs.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Handles user signup, authentication, and email verification.
 */
@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(UserRepository userRepository,
                                 BCryptPasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

     //create a new user account, generate a verification code, and send email.

    public User signup(RegisterUserDto input) {
        //  uniqueness checks
        userRepository.findByUsername(input.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already in use");
        });
        userRepository.findByEmail(input.getEmail()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already in use");
        });

        // build user and set verification fields
        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEnabled(false); // since the suer did not verify yet, default to false

        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));

        //  send verification email
        User saved = userRepository.save(user);
        sendVerificationEmail(saved);

        log.info("User registered, verification email sent to {}", saved.getEmail());
        return saved;
    }


     // authenticate an existing user by username and password
    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByUsername(input.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("User account is not verified");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));
        return user;
    }

     //verify user's email using the provided code enables the account on success
    public void verifyUser(VerifyUserDto input) {
        Optional<User> userOpt = userRepository.findByEmail(input.getEmail());
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found for email");
        }

        User user = userOpt.get();

        if (user.getVerificationCode() == null || user.getVerificationCodeExpiry() == null) {
            throw new IllegalStateException("no verification code found for user");
        }

        if (!user.getVerificationCode().equals(input.getVerificationCode())) {
            throw new IllegalArgumentException("invalid verification code");
        }

        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("verification code has expired");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

        log.info("User {} verified successfully", user.getEmail());
    }

    // Generate a 6-digit numeric code
    private String generateVerificationCode() {
        int code = 100000 + RANDOM.nextInt(900000);
        return Integer.toString(code);
    }

    // Send verification email with the current code via an HTML form
    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
                ".header { text-align: center; color: #333; margin-bottom: 30px; }" +
                ".verification-form { background-color: #f8f9fa; padding: 20px; border-radius: 8px; text-align: center; margin: 20px 0; }" +
                ".code-display { font-size: 32px; font-weight: bold; color: #007bff; letter-spacing: 3px; margin: 15px 0; }" +
                ".instructions { color: #666; margin-bottom: 20px; }" +
                ".footer { text-align: center; color: #888; font-size: 12px; margin-top: 30px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1 class='header'>Welcome to Book Recommendations!</h1>" +
                "<p>Hello " + user.getUsername() + ",</p>" +
                "<p>Thank you for registering! Please verify your email address to activate your account.</p>" +
                "<div class='verification-form'>" +
                "<h3>Your Verification Code</h3>" +
                "<div class='code-display'>" + verificationCode + "</div>" +
                "<p class='instructions'>Enter this code in the verification form to complete your registration.</p>" +
                "<p><strong>Note:</strong> This code will expire in 15 minutes.</p>" +
                "</div>" +
                "<p>If you didn't create this account, please ignore this email.</p>" +
                "<div class='footer'>" +
                "<p>This is an automated message from Book Recommendations System.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
                
        // Try to send the email to the user else print debug to terminal
        try {
            emailService.sendEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage());
            e.printStackTrace();
        }
    }

    // Add missing method for resending verification codes
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));
        
        if (user.isEnabled()) {
            throw new IllegalStateException("User account is already verified");
        }
        
        String newCode = generateVerificationCode();
        user.setVerificationCode(newCode);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));
        
        userRepository.save(user);
        sendVerificationEmail(user);
        
        log.info("Verification code resent to {}", user.getEmail());
    }
}
