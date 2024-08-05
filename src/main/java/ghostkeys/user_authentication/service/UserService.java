package ghostkeys.user_authentication.service;


import ghostkeys.user_authentication.dto.ChangePasswordRequest;
import ghostkeys.user_authentication.model.User;
import ghostkeys.user_authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder encoder;
    private final UserRepository repository;

    /**
     * Changes the password for the currently logged-in user.
     *
     * @param request       The request object containing current and new passwords.
     * @param connectedUser The Principal object representing the currently authenticated user.
     * @throws IllegalStateException If the current password is incorrect or the new passwords don't match.
     */
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("Password do not match");
        }

        if (!encoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Incorrect password");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        repository.save(user);
    }

}
