package authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.TextUtils;

import javax.naming.Binding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller to handle login requests
 */
@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public String login(
            @ModelAttribute("login") Login login
            , Model model) {

        User user = null;
        try {

            user = userService.authorize(login);
        }
        catch(Exception e) {
            model.addAttribute("failed_reason", e.getMessage());
            return "login";
        }

        return "redirect:lobby?apiKey=" + user.getUserApiKey();
    }
}
