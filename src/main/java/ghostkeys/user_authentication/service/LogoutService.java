package ghostkeys.user_authentication.service;

import ghostkeys.user_authentication.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /**
     * Performs logout by invalidating the JWT token.
     *
     * @param request the HTTP request containing the logout request.
     * @param response the HTTP response.
     * @param authentication the current authentication information (may be null).
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        // Extract the JWT token from the Authorization header
        jwt = authHeader.substring(7);

        // Retrieve the stored token from the repository
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);

        if (storedToken != null) {
            // Mark the token as expired and revoked
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);

            // Clear the security context
            SecurityContextHolder.clearContext();
        }
    }
}
