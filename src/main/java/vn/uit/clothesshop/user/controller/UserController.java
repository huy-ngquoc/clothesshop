package vn.uit.clothesshop.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vn.uit.clothesshop.user.presentation.form.UserCreationRequestDto;
import vn.uit.clothesshop.user.presentation.form.UserUpdateAvatarRequestDto;
import vn.uit.clothesshop.user.presentation.form.UserUpdateInfoRequestDto;
import vn.uit.clothesshop.user.presentation.form.UserUpdatePasswordRequestDto;
import vn.uit.clothesshop.user.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    @NotNull
    private final UserService userService;

    public UserController(
            @NotNull final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserPage(final Model model) {
        final var responseDtoList = this.userService.handleFindAllUsers();
        model.addAttribute("responseDtoList", responseDtoList);
        return "admin/user/show";
    }

    @GetMapping("/{id}")
    public String getUserDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var responseDto = this.userService.handleFindUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("responseDto", responseDto);
        return "admin/user/detail";
    }

    @GetMapping("/create")
    public String getUserCreationPage(final Model model) {
        final var requestDto = new UserCreationRequestDto();
        model.addAttribute("requestDto", requestDto);
        return "admin/user/create";
    }

    @PostMapping("/create")
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

    @GetMapping("/update/info/{id}")
    public String getUserUpdateInfoPage(
            final Model model,
            @PathVariable final long id) {
        final var middleDto = this.userService.handleCreateMiddleDtoForUpdateInfo(id);
        model.addAttribute("id", id);
        model.addAttribute("username", middleDto.getUsername());
        model.addAttribute("avatarFilePath", middleDto.getAvatarFilePath());
        model.addAttribute("requestDto", middleDto.getRequestDto());
        return "admin/user/update/info";
    }

    @PostMapping("/update/info/{id}")
    public String updateUserInfo(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") @Valid final UserUpdateInfoRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/update/info";
        }

        this.userService.handleUpdateUserInfo(id, requestDto);
        return "redirect:/admin/user";
    }

    @GetMapping("/update/password/{id}")
    public String getUserUpdatePasswordPage(
            final Model model,
            @PathVariable final long id) {
        final var requestDto = new UserUpdatePasswordRequestDto();
        model.addAttribute("id", id);
        model.addAttribute("requestDto", requestDto);
        return "admin/user/update/password";
    }

    @PostMapping("/update/password/{id}")
    public String updateUserPassword(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") @Valid final UserUpdatePasswordRequestDto requestDto,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/update/password";
        }

        this.userService.handleUpdateUserPassword(id, requestDto);
        return "redirect:/admin/user/update/info/" + id;
    }

    @GetMapping("/update/avatar/{id}")
    public String getUserUpdateAvatarPage(
            final Model model,
            @PathVariable final long id) {
        final var avatarFilePath = this.userService.findAvatarFilePathOfUserById(id);

        model.addAttribute("id", id);
        model.addAttribute("avatarFilePath", avatarFilePath);
        model.addAttribute("requestDto", new UserUpdateAvatarRequestDto());

        return "admin/user/update/avatar";
    }

    @PostMapping("/update/avatar/{id}")
    public String updateUserAvatar(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute("requestDto") final UserUpdateAvatarRequestDto requestDto) {
        this.userService.handleUpdateUserAvatar(id, requestDto.getAvatarFile());
        return "redirect:/admin/user/update/info/" + id;
    }

    @GetMapping("/update/avatar/delete/{id}")
    public String getUserUpdateAvatarDeletionPage(
            final Model model,
            @PathVariable final long id) {
        final var avatarFilePath = this.userService.findAvatarFilePathOfUserById(id);

        model.addAttribute("id", id);
        model.addAttribute("avatarFilePath", avatarFilePath);

        return "admin/user/update/avatarDeletion";
    }

    @PostMapping("/update/avatar/delete/{id}")
    public String updateUserAvatarDeletion(
            final Model model,
            @PathVariable final long id) {
        this.userService.handleUpdateUserAvatarDeletion(id);
        return "redirect:/admin/user/update/info/" + id;
    }

    @GetMapping("/delete/{id}")
    public String getUserDeletionConfirmPage(
            @PathVariable final long id,
            final Model model) {
        final var avatarFilePath = this.userService.findAvatarFilePathOfUserById(id);

        model.addAttribute("id", id);
        model.addAttribute("avatarFilePath", avatarFilePath);

        return "admin/user/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteUserById(
            @PathVariable final long id,
            final Model model) {
        this.userService.deleteUserById(id);
        return "redirect:/admin/user";
    }
}
