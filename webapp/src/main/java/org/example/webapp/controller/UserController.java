package org.example.webapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.UserDto;
import org.example.webapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", UserDto.builder().build());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("user") UserDto userDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "auth/signup";

        userService.register(userDto);
        model.addAttribute("success", true);
        model.addAttribute("user", UserDto.builder().build());
        return "auth/signup";
    }
}
