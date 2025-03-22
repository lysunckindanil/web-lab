package org.example.webapp.controller.forum;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.issue.CreateIssueDto;
import org.example.webapp.dto.forum.issue.DeleteIssueDto;
import org.example.webapp.service.CommentService;
import org.example.webapp.service.ForumService;
import org.example.webapp.service.IssueService;
import org.example.webapp.util.BadRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forum/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;
    private final CommentService commentService;
    private final ForumService forumService;

    @GetMapping("/{id}")
    public String issue(@PathVariable("id") Long issueId, Model model) {
        model.addAttribute("issue", issueService.getById(issueId));
        model.addAttribute("comments", commentService.getByIssueId(issueId));
        model.addAttribute("isRedactor", forumService.isRedactor());
        return "forum/issue/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("issue", new CreateIssueDto());
        return "forum/issue/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("issue") CreateIssueDto issue, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors())
            return "forum/issue/create";
        model.addAttribute("success", true);
        issueService.create(issue);
        return "forum/issue/create";
    }

    @PostMapping("/delete")
    public String create(@Valid @ModelAttribute DeleteIssueDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        issueService.delete(dto);
        return "redirect:/forum";
    }
}
