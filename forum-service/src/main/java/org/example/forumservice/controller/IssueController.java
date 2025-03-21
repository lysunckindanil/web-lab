package org.example.forumservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
import org.example.forumservice.dto.issue.IssueDto;
import org.example.forumservice.entity.Issue;
import org.example.forumservice.service.IssueService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @GetMapping
    public List<IssueDto> getIssues() {
        return issueService.findAll().stream().map(IssueController::toIssueDto).toList();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> deleteIssue(@Valid @RequestBody CreateIssueDto issueDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        issueService.create(issueDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteIssue(@Valid @RequestBody DeleteIssueDto deleteIssueDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(bindingResult.getAllErrors().stream().findAny().get().getDefaultMessage());
        issueService.delete(deleteIssueDto);
        return ResponseEntity.noContent().build();
    }

    private static IssueDto toIssueDto(Issue issue) {
        return IssueDto.builder()
                .id(issue.getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .createdAt(issue.getCreatedAt())
                .authorUsername(issue.getAuthor().getUsername())
                .build();
    }
}
