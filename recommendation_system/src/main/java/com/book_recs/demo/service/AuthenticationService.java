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

     //Create a new user account, generate a verification code, and send email.

    public User signup(RegisterUserDto input) {
        // Basic uniqueness checks
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
        user.setEnabled(false);

        String code = generateVerificationCode();
        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(LocalDateTime.now().plusMinutes(15));

        //  send verification email
        User saved = userRepository.save(user);
        sendVerificationEmail(saved);

        log.info("User registered, verification email sent to {}", saved.getEmail());
        return saved;
    }


     // Authenticate an existing user by username and password
     
    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEnabled()) {
            throw new DisabledException("User account is not verified");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));

        return user;
    }

    /**
     * Verify user's email using the provided code enables the account on success
     */
    public void verifyUser(VerifyUserDto input) {
        Optional<User> userOpt = userRepository.findByEmail(input.getEmail());
        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found for email");
        }

        User user = userOpt.get();

        if (user.getVerificationCode() == null || user.getVerificationCodeExpiry() == null) {
            throw new IllegalStateException("No verification code found for user");
        }

        if (!user.getVerificationCode().equals(input.getVerificationCode())) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        if (user.getVerificationCodeExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification code has expired");
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

    // Send verification email with the current code
    private void sendVerificationEmail(User user) {
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationCode());
    }
}
