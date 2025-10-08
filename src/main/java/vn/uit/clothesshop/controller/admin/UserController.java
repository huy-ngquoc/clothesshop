package vn.uit.clothesshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.service.UserService;

@Controller
public class UserController {
    @NotNull
    private final UserService userService;

    public UserController(
            @NotNull final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/user")
    public String getUserPage(final Model model) {
        final var responseDtoList = this.userService.handleFindAllUsers();
        model.addAttribute("responseDtoList", responseDtoList);
        return "admin/user/show";
    }
}
