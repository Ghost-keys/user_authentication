package ghostkeys.user_authentication.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ghostkeys.user_authentication.model.Permission.*;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_CREATE,
                    MANAGER_DELETE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_CREATE,
                    MANAGER_DELETE
            )
    );

    private final Set<Permission> permissions;


    /**
     * Retrieves authorities (permissions) associated with the user role.
     *
     * @return List of SimpleGrantedAuthority objects representing the authorities of the role.
     */
    public List<SimpleGrantedAuthority> getAuthorities(){
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}
