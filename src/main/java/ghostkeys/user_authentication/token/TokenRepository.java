package ghostkeys.user_authentication.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Token entities.
 */
public interface TokenRepository extends JpaRepository<Token, Integer> {

    /**
     * Finds all valid tokens (i.e., tokens that are not expired or revoked) for a given user.
     *
     * @param id the ID of the user whose valid tokens are to be retrieved
     * @return a list of valid Token entities associated with the specified user
     */
    @Query(value = """ 
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Integer id);

    /**
     * Finds a token by its token string value.
     *
     * @param token the string value of the token to be retrieved
     * @return an Optional containing the Token entity if found, or empty if not found
     */
    Optional<Token> findByToken(String token);
}
