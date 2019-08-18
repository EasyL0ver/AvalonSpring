package lobby;

import authentication.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Set;
import java.util.UUID;

@Controller
public class LobbyInformationController {

    private UserService userService;
    private Set<UUID> registeredUsers;

    @Autowired
    public LobbyInformationController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/lobbyreg")
    //@SendTo("/topic/greetings")
    public void register(UserSpecificMessage registration) throws Exception {
        UUID apiKey = registration.getUserApiKey();
        Boolean apiKeyIsValid = userService.getActiveUsers().containsKey(apiKey);

        //todo something better
        if(!apiKeyIsValid)
            return;

        registeredUsers.add(apiKey);
    }
}
