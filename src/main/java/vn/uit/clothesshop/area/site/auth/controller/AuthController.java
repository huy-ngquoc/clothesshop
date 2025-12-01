package vn.uit.clothesshop.area.site.auth.controller;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import vn.uit.clothesshop.area.shared.auth.JwtTokenProvider;
import vn.uit.clothesshop.area.shared.util.CookieUtil;
import vn.uit.clothesshop.feature.user.domain.User.Role;
import vn.uit.clothesshop.feature.user.presentation.form.LoginRequest;
import vn.uit.clothesshop.feature.user.presentation.form.RegisterDto;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authManager;

    public AuthController(
            final AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @GetMapping("/register")
    public String getRegisterPage(final Model model) {
        final var redirectUrl = AuthController.getRedirectUrlIfLoggedIn();
        if (redirectUrl != null) {
            return redirectUrl;
        }

        final RegisterDto registerDto = new RegisterDto();
        model.addAttribute("registerDto", registerDto);
        return "client/homepage/register";
    }

    @PostMapping("/register")
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

    @GetMapping("/login")
    public String getLoginPage(final Model model) {
        final var redirectUrl = AuthController.getRedirectUrlIfLoggedIn();
        if (redirectUrl != null) {
            return redirectUrl;
        }

        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginForm", loginRequest);
        return "client/homepage/login";
    }

    @PostMapping("/login")
    public String login(
            final Model model,
            @ModelAttribute("loginForm") @Valid final LoginRequest form,
            final BindingResult bindingResult,
            final HttpServletResponse response) throws IOException {
        if (bindingResult.hasErrors()) {
            return "client/homepage/login";
        }

        try {
            final var auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));

            final var user = (UserDetails) auth.getPrincipal();
            final var username = user.getUsername();

            // Tạo token
            final var accessToken = JwtTokenProvider.generateAccessToken(username, user.getAuthorities());
            final var refreshToken = JwtTokenProvider.generateRefreshToken(username);
            CookieUtil.addAuthCookies(response, accessToken, refreshToken);

            return determineRedirectUrl(user.getAuthorities());

        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid credential");
            return "client/homepage/login";
        }
    }

    @GetMapping("/logout")
    public String logout(
            final Model model,
            final HttpServletResponse response) {
        CookieUtil.clearAuthCookies(response);

        return "redirect:/";
    }

    private static String getRedirectUrlIfLoggedIn() {
        final var auth = SecurityContextHolder.getContext().getAuthentication();

        if ((auth == null) || (!auth.isAuthenticated()) || (auth instanceof AnonymousAuthenticationToken)) {
            return null;
        }

        return determineRedirectUrl(auth.getAuthorities());
    }

    private static String determineRedirectUrl(Collection<? extends GrantedAuthority> authorities) {
        var redirectUrl = "/";

        final var isAdmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.getAuthority()));

        if (isAdmin) {
            redirectUrl = "/admin";
        }

        return "redirect:" + redirectUrl;
    }
}
