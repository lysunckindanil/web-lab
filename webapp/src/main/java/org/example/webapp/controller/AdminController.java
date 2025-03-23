package org.example.webapp.controller;

import lombok.RequiredArgsConstructor;
import org.example.webapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "admin/statistics";
    }

    @ResponseBody
    @PostMapping("/grantRedactor")
    public ResponseEntity<Void> grantRedactor(@RequestParam String username) {
        userService.grantAuthority("ROLE_REDACTOR", username);
        return ResponseEntity.noContent().build();
    }

    @ResponseBody
    @PostMapping("/revokeRedactor")
    public ResponseEntity<Void> revokeRedactor(@RequestParam String username) {
        userService.revokeAuthority("ROLE_REDACTOR", username);
        return ResponseEntity.noContent().build();
    }

}

