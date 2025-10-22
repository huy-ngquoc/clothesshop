package vn.uit.clothesshop.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/auth/register")
    public String getRegisterPage(final Model model) {
        return "client/homepage/register";
    }
}
