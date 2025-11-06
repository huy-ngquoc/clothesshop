package vn.uit.clothesshop.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import vn.uit.clothesshop.user.presentation.form.LoginRequest;
import vn.uit.clothesshop.user.presentation.form.RegisterDto;
import vn.uit.clothesshop.user.service.UserService;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth/register")
    public String getRegisterPage(final Model model) {
        final RegisterDto registerDto = new RegisterDto();
        model.addAttribute("registerDto", registerDto);
        return "client/homepage/register";
    }

    @PostMapping("/auth/register")
    public String register(
            @ModelAttribute("registerDto") @Valid final RegisterDto registerDto,
            final BindingResult bindingResult,
            final Model model) {
        if (bindingResult.hasErrors()) {
            return "client/homepage/register";
        }

        // TODO: add method here
        // userService.userRegister(registerDto);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")
    public String getLoginPage(final Model model) {
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginForm", loginRequest);
        return "client/homepage/login";
    }
}
