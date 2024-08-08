package ghostkeys.user_authentication.modelTests;

import ghostkeys.user_authentication.model.User;
import ghostkeys.user_authentication.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user = User.builder()
            .id(1)
            .email("test@example.com")
            .password("testPassword")
            .role(UserRole.ADMIN)
            .build();

    @Test
    public void testAuthorities_AdminRole() {

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    @Test
    public void testAuthorities_ManagerRole() {
        user.setRole(UserRole.MANAGER);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    @Test
    public void testAuthorities_UserRole() {
        user.setRole(UserRole.USER);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ROLE_MANAGER")));
    }

    @Test
    public void testGetPassword() {
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testGetUserName() {
        assertEquals("test@example.com", user.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(user.isEnabled());
    }
}
