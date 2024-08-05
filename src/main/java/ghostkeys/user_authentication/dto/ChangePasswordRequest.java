package ghostkeys.user_authentication.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Data Transfer Object for handling change password requests.
 * This class holds the necessary information required for a user to change their password.
 */
@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
