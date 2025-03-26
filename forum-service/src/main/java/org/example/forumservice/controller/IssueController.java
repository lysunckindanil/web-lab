package org.example.forumservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.issue.*;
import org.example.forumservice.service.issue.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @PostMapping
    public List<IssueDto> getIssues(@Valid @RequestBody GetIssuesDto dto) {
        return issueService.findAll(dto);
    }

    @PostMapping("/getById")
    public IssueDto getById(@Valid @RequestBody GetIssueByIdDto dto) {
        return issueService.findById(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateIssueDto issueDto) {
        issueService.create(issueDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteIssueDto deleteIssueDto) {
        issueService.delete(deleteIssueDto);
        return ResponseEntity.noContent().build();
    }
}
