package org.example.webapp.client;

import org.example.webapp.dto.forum.issue.CreateIssueApiDto;
import org.example.webapp.dto.forum.issue.DeleteIssueApiDto;
import org.example.webapp.dto.forum.issue.GetIssueByIdApiDto;
import org.example.webapp.dto.forum.issue.IssueDto;

import java.util.List;

public interface IssueRestClient {
    List<IssueDto> getIssues();

    IssueDto getById(GetIssueByIdApiDto dto);

    void create(CreateIssueApiDto dto);

    void delete(DeleteIssueApiDto dto);
}
