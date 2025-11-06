package main.java.com.book_recs.demo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO used for email verification
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyUserDto {
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;

    @NotBlank(message = "verification code is required")
    private String verificationCode;
}
