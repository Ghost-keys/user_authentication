package ghostkeys.user_authentication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghostkeys.user_authentication.dto.AuthenticationRequest;
import ghostkeys.user_authentication.dto.AuthenticationResponse;
import ghostkeys.user_authentication.dto.RegistrationRequest;
import ghostkeys.user_authentication.model.User;
import ghostkeys.user_authentication.repository.UserRepository;
import ghostkeys.user_authentication.token.Token;
import ghostkeys.user_authentication.token.TokenRepository;
import ghostkeys.user_authentication.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user details
     * @return AuthenticationResponse containing access and refresh tokens
     */
    public AuthenticationResponse register(RegistrationRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param request the authentication request containing email and password
     * @return AuthenticationResponse containing access and refresh tokens
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = repository.findUserByEmail(request.getEmail()).orElseThrow();
        System.out.println(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * Revokes all valid tokens for a user.
     *
     * @param user the user whose tokens are to be revoked
     */
    private void revokeAllUserTokens(User user) {
        var validUserToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(token ->{
            token.setExpired(true);
            token.setRevoked(true);
        });
    }


    /**
     * Saves a new token for a user.
     *
     * @param user the user for whom the token is to be saved
     * @param jwtToken the JWT token to be saved
     */
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    /**
     * Refreshes the JWT access token using a refresh token.
     *
     * @param request the HTTP request containing the refresh token
     * @param response the HTTP response to write the new tokens to
     * @throws IOException if an input or output exception occurred
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findUserByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)){
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
