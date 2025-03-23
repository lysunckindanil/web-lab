package org.example.webapp.service.issue;

import org.example.webapp.dto.forum.issue.CreateIssueDto;
import org.example.webapp.dto.forum.issue.DeleteIssueDto;
import org.example.webapp.dto.forum.issue.IssueDto;

import java.util.List;

public interface IssueService {
    List<IssueDto> getIssues();

    IssueDto getById(Long id);

    void create(CreateIssueDto issue);

    void delete(DeleteIssueDto issue);
}
