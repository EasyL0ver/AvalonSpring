package common;

import authentication.User;
import authentication.UserService;
import lobby.dto.UserSpecificMessage;

public abstract class UserSpecificMessageController {

    private final UserService userService;

    public UserSpecificMessageController(UserService userService){
        this.userService = userService;
    }

    /**
     * finds user according to message sent
     * @param userSpecificMessage
     * @return user
     */
    protected User FindUser(UserSpecificMessage userSpecificMessage){
        return userService.findUser(userSpecificMessage.getUserApiKey());
    }

}
