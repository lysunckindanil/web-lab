package org.example.webapp.client;

import org.example.webapp.dto.forum.issue.*;

import java.util.List;

public interface IssueRestClient {
    List<IssueDto> getIssues(GetIssuesApiDto dto);

    IssueDto getById(GetIssueByIdApiDto dto);

    void create(CreateIssueApiDto dto);

    void delete(DeleteIssueApiDto dto);
}
