package my.project.web;


import my.project.domain.User;
import my.project.service.interfaces.SecurityService;
import my.project.service.interfaces.UserService;
import my.project.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created 06.02.17.
 *
 * @author Max Goncharov
 */
@Controller
public class UserController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserValidator userValidator;

    @Autowired
    public UserController(@Qualifier("userServiceImpl") UserService userService,
                          @Qualifier("securityServiceImpl") SecurityService securityService,
                          @Qualifier("userValidator") UserValidator userValidator) {
        this.userService = userService;
        this.securityService = securityService;
        this.userValidator = userValidator;
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registrationUser";
    }



    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user,
                          BindingResult bindingResult,
                          Model model
                          ) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registrationUser";
        }

        user.setRoleId(1L);
        userService.addUser(user);
        securityService.autoLogin(user.getLogin(), user.getPassword());
        return "redirect:/ad";
    }




    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Username or password is incorrect");
        }

        if (logout != null) {
            model.addAttribute("message", "Logged out successfully");
        }

        return "login";
    }



    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(Model model) {
        return "welcome";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin (Model model) {
        return "admin";
    }




    @RequestMapping("/index")
    public String home(Map<String, Object> map) {
        return "redirect:/ad";
    }

    @RequestMapping("/")
    public String reHome(Map<String, Object> map) {
        return "redirect:/ad";
    }


}
