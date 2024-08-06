package ghostkeys.user_authentication.controller;


import ghostkeys.user_authentication.dto.AuthenticationRequest;
import ghostkeys.user_authentication.dto.AuthenticationResponse;
import ghostkeys.user_authentication.dto.RegistrationRequest;
import ghostkeys.user_authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST controller for handling authentication-related requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    /**
     * Handles user registration requests.
     *
     * @param request the registration request containing user details
     * @return ResponseEntity containing the authentication response with access and refresh tokens
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(service.register(request));
    }


    /**
     * Handles user authentication requests.
     *
     * @param request the authentication request containing email and password
     * @return ResponseEntity containing the authentication response with access and refresh tokens
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    /**
     * Handles refresh token requests to generate new access tokens.
     *
     * @param request the HTTP request containing the refresh token in the Authorization header
     * @param response the HTTP response to write the new access and refresh tokens to
     * @throws IOException if an input or output exception occurred
     */
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request,response);
    }
}
