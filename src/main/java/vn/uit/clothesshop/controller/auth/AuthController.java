package vn.uit.clothesshop.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import vn.uit.clothesshop.dto.request.RegisterDto;
import vn.uit.clothesshop.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/auth/register")
    public String getRegisterPage(final Model model) {
        RegisterDto registerDto= new RegisterDto();
        model.addAttribute("registerDto", registerDto);
        return "client/homepage/register";
    }

    @PostMapping("/auth/register")
    public String register(@ModelAttribute("registerDto") @Valid RegisterDto registerDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "client/homepage/register";
        }
        userService.userRegister(registerDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login") 
    public String getLoginPage(final Model model) {
        return "client/homepage/login";
    }
}
