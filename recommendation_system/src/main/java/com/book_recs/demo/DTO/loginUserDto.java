package main.java.com.book_recs.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// DTO used for login requests
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;
}
