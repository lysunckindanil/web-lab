package org.example.forumservice.service;

import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
import org.example.forumservice.model.Issue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IssueService {
    Optional<Issue> findById(Long id);

    List<Issue> findAll();

    @Transactional
    void create(CreateIssueDto dto);

    @Transactional
    void delete(DeleteIssueDto dto);
}
