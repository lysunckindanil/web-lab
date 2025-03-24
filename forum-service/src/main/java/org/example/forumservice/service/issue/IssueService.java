package org.example.forumservice.service.issue;

import org.example.forumservice.dto.issue.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IssueService {
    IssueDto findById(GetIssueByIdDto dto);

    List<IssueDto> findAll(GetIssuesDto dto);

    void create(CreateIssueDto dto);

    void delete(DeleteIssueDto dto);
}
