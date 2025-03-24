package org.example.forumservice.service.issue;

import org.example.forumservice.dto.issue.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IssueService {
    IssueDto findById(GetIssueByIdDto dto);

    List<IssueDto> findAll(GetIssuesDto dto);

    @Transactional
    void create(CreateIssueDto dto);

    @Transactional
    void delete(DeleteIssueDto dto);
}
