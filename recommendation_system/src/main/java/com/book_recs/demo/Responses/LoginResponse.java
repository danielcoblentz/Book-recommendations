package main.java.com.book_recs.demo.responses;

// auto getters and setters
@Getter
@Setter
public class LoginResponse {
    private String token;
    private long expiresIn;

    public LoginResponse(String token, long expiresIn){
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
