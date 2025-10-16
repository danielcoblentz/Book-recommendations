package main.java.com.book_recs.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 100)
    private String username;
    
    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @columm(name = "verificationCode")
    private String verificationCode;

    @Column(name = verification_expiration, nullable = false)
    private LocalDateTime verificationCodeExpiredAt;


    public User() {
        this.username = usermame;
        this.email = email;
        this.password = password;
    }

    //defualt constructor
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}