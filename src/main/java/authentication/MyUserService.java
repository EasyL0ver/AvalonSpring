package authentication;

import org.graalvm.compiler.lir.LIRInstruction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MyUserService implements UserService {

    private final UserFactory userFactory;
    private final AuthenticationService authenticationService;
    private final Map<UUID, User> activeUsers;

    @Autowired
    public MyUserService(
            UserFactory userFactory
            , AuthenticationService authenticationService) {

        this.userFactory = userFactory;
        this.authenticationService = authenticationService;

        activeUsers = new HashMap<>();
    }

    @Override
    public User authorize(Login login) {

        if(login.getPassword() == null || login.getPassword().isEmpty())
            throw new SecurityException("login password is either null or empty");

        if(login.getUsername() == null || login.getUsername().isEmpty())
            throw new SecurityException("login username is either null or empty");

        User thisUser = null;
        try{

            authenticationService.Authenticate(login);

            thisUser = userFactory.Build(login);

            activeUsers.put(thisUser.getUserApiKey(), thisUser);

        }catch (Exception e){
            //todo exception handling
            throw e;
        }

        return thisUser;
    }

    @Override
    public User findUser(UUID userApiKey) {

        if(!activeUsers.containsKey(userApiKey))
            return null;

        return activeUsers.get(userApiKey);
    }

    @Override
    public Map<UUID, User> getActiveUsers() {
        return Collections.unmodifiableMap(activeUsers);
    }
}
