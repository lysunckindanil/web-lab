package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.IssueRestClient;
import org.example.webapp.dto.forum.issue.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRestClient issueRestClient;

    public List<IssueDto> getIssues() {
        return issueRestClient.getIssues();
    }

    public IssueDto getById(Long id) {
        GetIssueByIdApiDto getByIdDto = GetIssueByIdApiDto.builder().issueId(id).build();
        return issueRestClient.getById(getByIdDto);
    }

    public void create(CreateIssueDto issue) {
        CreateIssueApiDto request = CreateIssueApiDto.builder()
                .author(SecurityContextHolder.getContext().getAuthentication().getName())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .build();
        issueRestClient.create(request);
    }

    public void delete(DeleteIssueDto issue) {
        DeleteIssueApiDto request = DeleteIssueApiDto.builder()
                .issueId(issue.getIssueId())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        issueRestClient.delete(request);
    }
}
