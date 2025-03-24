package org.example.forumservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.issue.*;
import org.example.forumservice.service.issue.IssueService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/issue")
@RequiredArgsConstructor
public class IssueController {
    private final IssueService issueService;

    @PostMapping
    public List<IssueDto> getIssues(@Valid @RequestBody GetIssuesDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        return issueService.findAll(dto);
    }

    @PostMapping("/getById")
    public IssueDto getById(@Valid @RequestBody GetIssueByIdDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        return issueService.findById(dto);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateIssueDto issueDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        issueService.create(issueDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@Valid @RequestBody DeleteIssueDto deleteIssueDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        issueService.delete(deleteIssueDto);
        return ResponseEntity.noContent().build();
    }
}
