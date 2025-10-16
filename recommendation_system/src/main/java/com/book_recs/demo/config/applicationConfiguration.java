import java.beans.BeanProperty;
import java.net.Authenticator;

@Configuration
public class applicationConfiguration {

    private final UserRepository userRepository;

    public applicationConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //4 beans to be injected
    //1. password encoder
    @Bean
    userDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //2. password encoder
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    //3. authentication manager
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //4. authentication provider
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
