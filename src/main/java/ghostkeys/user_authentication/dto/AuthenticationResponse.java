package ghostkeys.user_authentication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A Data Transfer Object (DTO) representing an authentication response.
 * This class is used to encapsulate the tokens provided after a successful authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    /**
     * The access token provided after successful authentication.
     * This token is used for authorizing subsequent requests.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * The refresh token provided after successful authentication.
     * This token is used to obtain a new access token when the current one expires.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;
}
