package authentication;

import java.util.List;

public interface UserService {
    User Authorize(Login login);

    List<User> getActiveUsers();
}
