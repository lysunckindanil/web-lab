package org.example.webapp.controller.forum;


import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.issue.CreateIssueDto;
import org.example.webapp.dto.forum.issue.DeleteIssueDto;
import org.example.webapp.service.comment.CommentService;
import org.example.webapp.service.issue.IssueService;
import org.example.webapp.util.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forum/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;
    private final CommentService commentService;

    @GetMapping("/{id}")
    public String issue(@PathVariable("id") String issueIdStr, Model model) {
        try {
            Long issueId = Long.parseLong(issueIdStr);
            model.addAttribute("issue", issueService.getById(issueId));
            model.addAttribute("comments", commentService.getByIssueId(issueId));
        } catch (Exception e) {
            return "redirect:/forum";
        }

        return "forum/issue/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("issue", CreateIssueDto.builder().build());
        return "forum/issue/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("issue") CreateIssueDto issue, Model model) {
        try {
            issueService.create(issue);
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "forum/issue/create";
        }
        model.addAttribute("success", true);
        return "forum/issue/create";
    }

    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<Void> delete(DeleteIssueDto dto) {
        issueService.delete(dto);
        return ResponseEntity.noContent().build();
    }
}
