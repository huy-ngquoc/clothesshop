package vn.uit.clothesshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.dto.request.UserCreationRequestDto;
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

    @GetMapping("/admin/user/{id}")
    public String getUserDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = this.userService.handleFindUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String getUserCreationPage(final Model model) {
        final var requestDto = new UserCreationRequestDto();
        model.addAttribute("requestDto", requestDto);
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String createUser(
            final Model model,
            @ModelAttribute("requestDto") @Valid final UserCreationRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/create";
        }

        this.userService.handleCreateUser(requestDto);
        return "redirect:/admin/user";
    }
}
