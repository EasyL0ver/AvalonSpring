package authentication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * interface that defines user authentication logic
 */
public interface UserService {
    User authorize(Login login) throws Exception;

    User findUser(UUID userApiKey);

}
