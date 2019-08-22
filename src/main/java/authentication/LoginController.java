package authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.TextUtils;

import javax.naming.Binding;
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
            , @ModelAttribute("login") Login login
            , BindingResult result
            , Model model) throws Exception {

        User user = null;
        try {

            user = userService.authorize(login);
        }
        catch(Exception e) {
            //todo better exception handling
            //todo add thymeleaf login failed reason
            model.addAttribute("failed_reason", e.getMessage());
            return "login";
            //response.sendRedirect("/");
        }


        model.addAttribute("api_key", user.getUserApiKey());

        return "lobby";
    }
}
