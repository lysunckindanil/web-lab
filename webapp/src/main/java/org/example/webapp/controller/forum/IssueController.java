package org.example.webapp.controller.forum;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.CreateIssueDto;
import org.example.webapp.dto.forum.DeleteIssueDto;
import org.example.webapp.service.IssueService;
import org.example.webapp.util.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/forum/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("issue", new CreateIssueDto());
        return "forum/issue/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute CreateIssueDto issue, Principal principal, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("issue", issue);
            if (bindingResult.getAllErrors().size() > 1)
                model.addAttribute("error", "Название и описание не должны быть пустыми.");
            else
                model.addAttribute("error", bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
            return "forum/issue/create";
        }

        issueService.create(issue, principal.getName());
        return "redirect:/forum";
    }

    @PostMapping("/delete")
    public String create(@Valid @ModelAttribute DeleteIssueDto dto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        issueService.delete(dto, principal.getName());
        return "redirect:/forum";
    }
}
