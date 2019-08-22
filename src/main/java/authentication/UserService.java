package authentication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    User authorize(Login login) throws Exception;

    User findUser(UUID userApiKey);

    Map<UUID, User> getActiveUsers();

}
