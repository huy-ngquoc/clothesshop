package vn.uit.clothesshop.area.site.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import vn.uit.clothesshop.area.shared.auth.JwtTokenProvider;
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
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginForm", loginRequest);
        return "client/homepage/login";
    }

    @PostMapping("/login")
    public String login(
            final Model model,
            @RequestBody @Valid final LoginRequest form,
            final HttpServletResponse response) {
        final var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword()));
        if (auth == null) {
            model.addAttribute("error", "Wrong account or password");
            return "client/homepage/login";
        }

        final var user = (UserDetails) auth.getPrincipal();
        final var username = user.getUsername();
        final var accessToken = JwtTokenProvider.generateAccessToken(username, user.getAuthorities());
        final var refreshToken = JwtTokenProvider.generateRefreshToken(username);

        final var accessCookie = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtTokenProvider.ACCESS_TOKEN_DURATION.toSeconds())
                .sameSite("Lax")
                .build();

        final var refreshCookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtTokenProvider.REFRESH_TOKEN_DURATION.toSeconds())
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(
            final Model model,
            @RequestBody @Valid final LoginRequest form,
            final HttpServletResponse response) {
        final var deleteAccessCookie = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        final var deleteRefreshCookie = ResponseCookie.from("REFRESH_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());

        return "redirect:/";
    }
}
