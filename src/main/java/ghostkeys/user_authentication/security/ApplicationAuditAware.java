package ghostkeys.user_authentication.security;

import ghostkeys.user_authentication.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Implementation of AuditorAware interface to provide the current auditor (user) for auditing purposes.
 * This class retrieves the current authenticated user's ID from the Spring Security context.
 */
public class ApplicationAuditAware implements AuditorAware<Integer> {

    /**
     * Retrieves the current auditor's ID.
     *
     * @return an Optional containing the current auditor's ID, or an empty Optional if no user is authenticated.
     */
    @NonNull
    @Override
    public Optional<Integer> getCurrentAuditor() {
        // Get the authentication object from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication is null, not authenticated, or is an instance of AnonymousAuthenticationToken
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Retrieve the user principal from the authentication object
        User userPrincipal = (User) authentication.getPrincipal();

        // Return the user ID wrapped in an Optional
        return Optional.ofNullable(userPrincipal.getId());
    }
}
