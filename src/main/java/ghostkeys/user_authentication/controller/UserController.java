package ghostkeys.user_authentication.controller;

import ghostkeys.user_authentication.dto.ChangePasswordRequest;
import ghostkeys.user_authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Controller for handling user-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    /**
     * Endpoint to change the password of the currently logged-in user.
     *
     * @param request        The request object containing current and new passwords.
     * @param connectedUser  The Principal object representing the currently authenticated user.
     * @return ResponseEntity indicating the success of the password change operation.
     */
    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
