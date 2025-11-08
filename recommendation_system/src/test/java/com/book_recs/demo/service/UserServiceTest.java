package test.java.com.book_recs.demo.service;

import com.book_recs.demo.model.User;
import com.book_recs.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetAllUsers() {
        // create a test user
        User testUser = new User();
        testUser.setUsername("servicetest");
        testUser.setEmail("servicetest@example.com");
        testUser.setPassword("password");
        testUser.setEnabled(true);
        
        userRepository.save(testUser);

        // test service method
        List<User> users = userService.allUsers();
        
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> "servicetest".equals(u.getUsername())));
    }
}
