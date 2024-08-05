package ghostkeys.user_authentication.repositoryTest;

import ghostkeys.user_authentication.model.User;
import ghostkeys.user_authentication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository repository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email("test@example.com")
                .build();
    }

    @Test
    public void testFindUserByEmail() {
        when(repository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        Optional<User> foundUser = repository.findUserByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }


}

