package com.example.demo.controllers;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.UserDto;
import com.example.demo.dtos.ChangePasswordRequest;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.InvalidUserStateException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.exceptions.WeakPasswordException;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller handling authentication and password management
 */
@Controller
@AllArgsConstructor
public class HomeController {

    private final UserService userService;

    /**
     * Redirect root URL to login page
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        // If already logged in, redirect to users list
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/ui/users";
        }
        return "redirect:/login";
    }

    /**
     * Display login page
     */
    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        // If already logged in, redirect to users list
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/ui/users";
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    /**
     * Handle login form submission
     */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        try {
            // Call service layer for authentication
            UserDto user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

            // Store user in session
            session.setAttribute("loggedInUser", user);

            redirectAttributes.addFlashAttribute("successMessage", "Welcome, " + user.getName() + "!");
            return "redirect:/ui/users";

        } catch (UserNotFoundException | InvalidCredentialsException | InvalidUserStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

    /**
     * Handle logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "You have been logged out successfully");
        return "redirect:/login";
    }

    /**
     * Display change password page
     */
    @GetMapping("/change-password")
    public String showChangePasswordPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to continue");
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        return "change-password";
    }

    /**
     * Handle change password form submission
     */
    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute ChangePasswordRequest request,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please log in to continue");
            return "redirect:/login";
        }

        try {
            // Call service layer for password change
            UserDto updatedUser = userService.changePassword(
                loggedInUser.getId(),
                request.getOldPassword(),
                request.getNewPassword()
            );

            // Update session with new user data
            session.setAttribute("loggedInUser", updatedUser);

            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
            return "redirect:/ui/users";

        } catch (InvalidCredentialsException | WeakPasswordException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/change-password";
        }
    }
}

