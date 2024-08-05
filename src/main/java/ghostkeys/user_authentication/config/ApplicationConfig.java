package ghostkeys.user_authentication.config;

import ghostkeys.user_authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository repository;

    /**
     * Provides a PasswordEncoder implementation that uses the BCrypt hashing function.
     * This is used to encode and verify user passwords securely.
     *
     * @return PasswordEncoder implementation.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a UserDetailsService implementation that loads user-specific data.
     * The method looks up a user by their email address from the UserRepository.
     *
     * @return UserDetailsService implementation.
     * @throws IllegalArgumentException if the user is not found.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findUserByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
