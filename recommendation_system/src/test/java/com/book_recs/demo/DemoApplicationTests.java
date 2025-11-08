package com.book_recs.demo;

import com.book_recs.demo.dto.RegisterUserDto;
import com.book_recs.demo.model.User;
import com.book_recs.demo.repository.UserRepository;
import com.book_recs.demo.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestPropertySource(properties = "spring.mail.host=localhost") // Mock email for tests
@Transactional // Roll back database changes after each test
class DemoApplicationTests {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		// Basic Spring context test
	}

	@Test
	void testUserSignup() {
		// Test user registration
		RegisterUserDto registerDto = new RegisterUserDto();
		registerDto.setUsername("testuser");
		registerDto.setEmail("test@example.com");
		registerDto.setPassword("password123");

		User user = authenticationService.signup(registerDto);

		assertNotNull(user);
		assertEquals("testuser", user.getUsername());
		assertEquals("test@example.com", user.getEmail());
		assertFalse(user.isEnabled()); // Should be disabled until verified
		assertNotNull(user.getVerificationCode());
	}

	@Test
	void testDuplicateUsernameThrowsException() {
		// Test that duplicate usernames are rejected
		RegisterUserDto registerDto = new RegisterUserDto();
		registerDto.setUsername("duplicate");
		registerDto.setEmail("first@example.com");
		registerDto.setPassword("password123");

		authenticationService.signup(registerDto);

		RegisterUserDto duplicateDto = new RegisterUserDto();
		duplicateDto.setUsername("duplicate");
		duplicateDto.setEmail("second@example.com");
		duplicateDto.setPassword("password123");

		assertThrows(IllegalArgumentException.class, () -> {
			authenticationService.signup(duplicateDto);
		});
	}

	@Test
	void testVerificationCodeGeneration() {
		// Test that verification codes are 6 digits
		RegisterUserDto registerDto = new RegisterUserDto();
		registerDto.setUsername("codetest");
		registerDto.setEmail("codetest@example.com");
		registerDto.setPassword("password123");

		User user = authenticationService.signup(registerDto);
		String code = user.getVerificationCode();

		assertNotNull(code);
		assertEquals(6, code.length());
		assertTrue(code.matches("\\d{6}")); // Should be 6 digits
	}

	@Test
	void testUserRepositoryFindByEmail() {
		// Test repository methods work correctly
		RegisterUserDto registerDto = new RegisterUserDto();
		registerDto.setUsername("repotest");
		registerDto.setEmail("repo@example.com");
		registerDto.setPassword("password123");

		authenticationService.signup(registerDto);

		assertTrue(userRepository.findByEmail("repo@example.com").isPresent());
		assertTrue(userRepository.findByEmail("notfound@example.com").isEmpty());
	}
}
