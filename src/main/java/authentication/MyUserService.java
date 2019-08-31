package authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

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
    public User authorize(Login login) throws Exception {

        if(login.getPassword() == null || login.getPassword().isEmpty())
            throw new SecurityException("login password is either null or empty");

        if(login.getUsername() == null || login.getUsername().isEmpty())
            throw new SecurityException("login username is either null or empty");

        User thisUser = null;
        try{

            authenticationService.Authenticate(login);

            thisUser = findLoggedInUser(login.getUsername());

            if(thisUser == null){
                thisUser = userFactory.Build(login);
                activeUsers.put(thisUser.getUserApiKey(), thisUser);
            }

        }catch (Exception e){
            e.printStackTrace();
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

    private User findLoggedInUser(String username){
        List<User> usersWithThisUsername = activeUsers.values().stream().filter(x -> x.getUserName().equals(username)).collect(Collectors.toList());

        if(usersWithThisUsername.size() == 1)
            return usersWithThisUsername.get(0);

        return null;
    }
}
