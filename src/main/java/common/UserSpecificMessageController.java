package common;

import authentication.User;
import authentication.UserService;
import lobby.dto.UserSpecificMessage;

public class UserSpecificMessageController {

    private final UserService userService;

    public UserSpecificMessageController(UserService userService){
        this.userService = userService;
    }

    protected User FindUser(UserSpecificMessage userSpecificMessage){
        return userService.findUser(userSpecificMessage.getUserApiKey());
    }

}
