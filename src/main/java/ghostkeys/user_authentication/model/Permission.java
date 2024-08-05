package ghostkeys.user_authentication.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing different user permissions within the application.
 * Each permission is associated with a specific action that can be performed by a user role.
 */
@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");

    private final String permissions;
}
