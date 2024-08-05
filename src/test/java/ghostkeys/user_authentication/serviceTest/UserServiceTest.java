package ghostkeys.user_authentication.serviceTest;

import ghostkeys.user_authentication.dto.ChangePasswordRequest;
import ghostkeys.user_authentication.model.User;
import ghostkeys.user_authentication.repository.UserRepository;
import ghostkeys.user_authentication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Principal connectedUser;


    @BeforeEach
    void set() {
        testUser = new User();
        testUser.setPassword("encodedPassword");

        connectedUser = new UsernamePasswordAuthenticationToken(testUser, null);
    }

    @Test
    void changePassword_WithValidRequest_ShouldChangePassword() {
        ChangePasswordRequest request = mock(ChangePasswordRequest.class);
        when(request.getCurrentPassword()).thenReturn("oldPassword");
        when(request.getNewPassword()).thenReturn("newPassword");
        when(request.getConfirmPassword()).thenReturn("newPassword");

        when(encoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
        when(encoder.encode("newPassword")).thenReturn("encodeNewPassword");

        userService.changePassword(request, connectedUser);

        assertEquals("encodeNewPassword", testUser.getPassword());
        verify(repository).save(testUser);
    }

    @Test
    void changePassword_WithNonMatchingNewPasswords_ShouldThrowException() {
        ChangePasswordRequest invalidRequest = mock(ChangePasswordRequest.class);
        when(invalidRequest.getNewPassword()).thenReturn("newPassword");
        when(invalidRequest.getConfirmPassword()).thenReturn("differentPassword");

        Exception exception = assertThrows(IllegalStateException.class, () ->
                userService.changePassword(invalidRequest, connectedUser));

        assertEquals("Password do not match", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void changePassword_WithIncorrectCurrentPassword_ShouldThrowException() {
        ChangePasswordRequest request = mock(ChangePasswordRequest.class);
        when(request.getCurrentPassword()).thenReturn("wrongPassword");
        when(request.getNewPassword()).thenReturn("newPassword");
        when(request.getConfirmPassword()).thenReturn("newPassword");

        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                userService.changePassword(request, connectedUser)
        );

        assertEquals("Incorrect password", exception.getMessage());
        verify(repository, never()).save(any());
    }

}
