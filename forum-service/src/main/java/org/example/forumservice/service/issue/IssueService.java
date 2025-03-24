package org.example.forumservice.service.issue;

import org.example.forumservice.dto.issue.*;
import org.example.forumservice.model.Issue;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IssueService {
    IssueDto findById(GetIssueByIdDto dto);

    Optional<Issue> findById(Long issueId);

    List<IssueDto> findAll(GetIssuesDto dto);

    @Transactional
    void create(CreateIssueDto dto);

    @Transactional
    void delete(DeleteIssueDto dto);
}
