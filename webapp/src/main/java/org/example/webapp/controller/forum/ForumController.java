package org.example.webapp.controller.forum;

import lombok.RequiredArgsConstructor;
import org.example.webapp.model.User;
import org.example.webapp.service.IssueService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {

    private final UserDetailsService userDetailsService;
    private final IssueService issueService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("issues", issueService.getIssues());
        return "forum/index";
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("roles", user.getRoles().toString()
                .replace("[", "")
                .replace("]", "")
                .replace("ROLE_", ""));
        return "forum/profile";
    }

}
