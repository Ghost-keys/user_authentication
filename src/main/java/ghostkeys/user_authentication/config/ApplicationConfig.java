package ghostkeys.user_authentication.config;

import ghostkeys.user_authentication.repository.UserRepository;
import ghostkeys.user_authentication.security.ApplicationAuditAware;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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


//    /**
//     * Creates and configures a DaoAuthenticationProvider bean.
//     * This provider uses a UserDetailsService to retrieve user information
//     * and a PasswordEncoder to verify passwords.
//     *
//     * @return an AuthenticationProvider configured with user details service and password encoder.
//     */
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvide = new DaoAuthenticationProvider();
//        authProvide.setUserDetailsService(userDetailsService());
//        authProvide.setPasswordEncoder(passwordEncoder());
//        return authProvide;
//    }


    /**
     * Creates and configures an AuditorAware bean.
     * This bean provides the current auditor (typically the current user)
     * for auditing purposes, such as setting createdBy and lastModifiedBy fields.
     *
     * @return an AuditorAware implementation that supplies the current auditor's ID.
     */
    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new ApplicationAuditAware();
    }


    /**
     * Creates and configures an AuthenticationManager bean.
     * This bean provides the AuthenticationManager which is responsible for
     * processing authentication requests.
     *
     * @param config the AuthenticationConfiguration used to configure the AuthenticationManager.
     * @return the configured AuthenticationManager instance.
     * @throws Exception if an error occurs while configuring the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
