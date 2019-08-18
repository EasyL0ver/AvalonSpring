package authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.TextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public String login(
            HttpServletRequest loginRequest
            , HttpServletResponse response
            , Model model) throws Exception {

        User user = null;
        try {

            String username = loginRequest.getParameter("username");
            String password = loginRequest.getParameter("password");


            Login login = new Login(username, password);

            user = userService.authorize(login);
        }
        catch(Exception e) {
            //todo better exception handling
            throw e;
        }


        model.addAttribute("api_key", user.getUserApiKey());

        return "lobby";
    }
}
