package vn.uit.clothesshop.user.controller;

import java.util.Objects;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import vn.uit.clothesshop.shared.constant.ModelAttributeConstant;
import vn.uit.clothesshop.shared.constraint.PagingConstraint;
import vn.uit.clothesshop.shared.util.PageableSanitizer;
import vn.uit.clothesshop.user.presentation.form.UserCreationForm;
import vn.uit.clothesshop.user.domain.User;
import vn.uit.clothesshop.user.domain.specification.UserSpecification;
import vn.uit.clothesshop.user.presentation.form.UserAvatarUpdateForm;
import vn.uit.clothesshop.user.presentation.form.UserInfoUpdateForm;
import vn.uit.clothesshop.user.presentation.form.UserPasswordUpdateForm;
import vn.uit.clothesshop.user.service.UserService;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    private static final Set<String> ALLOWED_SORT = Set.of(
            User.Fields.id,
            User.Fields.username,
            User.Fields.firstName,
            User.Fields.lastName,
            User.Fields.role);
    private static final PageableSanitizer pageableSanitizer;

    private final UserService userService;

    static {
        pageableSanitizer = new PageableSanitizer(ALLOWED_SORT, User.Fields.id);
    }

    public UserController(
            final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUserPage(
            final Model model,
            @RequestParam(required = false) @Nullable final String q,
            @PageableDefault(size = PagingConstraint.DEFAULT_SIZE) @NonNull final Pageable pageable) {
        final var safePageable = UserController.pageableSanitizer.sanitize(pageable);
        final var spec = UserSpecification.usernameLike(q)
                .or(UserSpecification.firstNameLike(q))
                .or(UserSpecification.lastNameLike(q));

        final var page = this.userService.findAllBasic(spec, safePageable);

        model.addAttribute(ModelAttributeConstant.PAGE, page);

        return "admin/user/show";
    }

    @GetMapping("/{id}")
    public String getUserDetailPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.userService.findDetailById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/user/detail";
    }

    @GetMapping("/create")
    public String getUserCreationPage(final Model model) {
        final var form = new UserCreationForm();

        model.addAttribute(ModelAttributeConstant.FORM, form);

        return "admin/user/create";
    }

    @PostMapping("/create")
    public String createUser(
            final Model model,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final UserCreationForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/create";
        }

        this.userService.create(form);
        return "redirect:/admin/user";
    }

    @GetMapping("/update/info/{id}")
    public String getUserUpdateInfoPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModelAndForm = this.userService.findInfoUpdateById(id)
                .orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);

        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }

        return "admin/user/update/info";
    }

    @PostMapping("/update/info/{id}")
    public String updateUserInfo(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final UserInfoUpdateForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var viewModel = this.userService.findInfoUpdateViewModelById(id).orElse(null);

            model.addAttribute(ModelAttributeConstant.ID, id);
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

            return "admin/user/update/info";
        }

        this.userService.updateInfoById(id, form);
        return "redirect:/admin/user";
    }

    @GetMapping("/update/password/{id}")
    public String getUserUpdatePasswordPage(
            final Model model,
            @PathVariable final long id) {
        final var form = this.userService.findPasswordUpdateFormById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.FORM, form);

        return "admin/user/update/password";
    }

    @PostMapping("/update/password/{id}")
    public String updateUserPassword(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final UserPasswordUpdateForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/user/update/password";
        }

        this.userService.updatePasswordById(id, form);
        return "redirect:/admin/user/update/info/" + id;
    }

    @GetMapping("/update/avatar/{id}")
    public String getUserUpdateAvatarPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModelAndForm = this.userService.findAvatarUpdateById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);

        if (viewModelAndForm != null) {
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModelAndForm.getFirst());
            model.addAttribute(ModelAttributeConstant.FORM, viewModelAndForm.getSecond());
        }

        return "admin/user/update/avatar";
    }

    @PostMapping("/update/avatar/{id}")
    public String updateUserAvatar(
            final Model model,
            @PathVariable final long id,
            @ModelAttribute(ModelAttributeConstant.FORM) @Valid @NonNull final UserAvatarUpdateForm form,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final var viewModel = this.userService.findAvatarUpdateViewModelById(id).orElse(null);

            model.addAttribute(ModelAttributeConstant.ID, id);
            model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

            return "admin/user/update/avatar";
        }

        this.userService.updateAvatarById(id, form);
        return "redirect:/admin/user/update/info/" + id;
    }

    @GetMapping("/update/avatar/delete/{id}")
    public String getUserUpdateAvatarDeletionPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.userService.findAvatarDeletionById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/user/update/avatarDeletion";
    }

    @PostMapping("/update/avatar/delete/{id}")
    public String updateUserAvatarDeletion(
            final Model model,
            @PathVariable final long id) {
        this.userService.deleteAvatarById(id);
        return "redirect:/admin/user/update/info/" + id;
    }

    @GetMapping("/delete/{id}")
    public String getUserDeletionConfirmPage(
            final Model model,
            @PathVariable final long id) {
        final var viewModel = this.userService.findAvatarDeletionById(id).orElse(null);

        model.addAttribute(ModelAttributeConstant.ID, id);
        model.addAttribute(ModelAttributeConstant.VIEW_MODEL, viewModel);

        return "admin/user/delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteUserById(
            final Model model,
            @PathVariable final long id) {
        this.userService.deleteById(id);
        return "redirect:/admin/user";
    }
}
