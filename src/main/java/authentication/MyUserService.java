package authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class MyUserService implements UserService {

    private final UserFactory userFactory;
    private final AuthenticationService authenticationService;
    private final LinkedList<User> activeUsers;

    @Autowired
    public MyUserService(
            UserFactory userFactory
            , AuthenticationService authenticationService) {

        this.userFactory = userFactory;
        this.authenticationService = authenticationService;

        activeUsers = new LinkedList<>();
    }

    @Override
    public User Authorize(Login login) {
        User thisUser = null;
        try{

            authenticationService.Authenticate(login);

            thisUser = userFactory.Build(login);

        }catch (Exception e){
            //todo exception handling
        }

        return thisUser;
    }

    @Override
    public List<User> getActiveUsers() {
        return Collections.unmodifiableList(activeUsers);
    }
}
