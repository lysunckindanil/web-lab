package org.example.webapp.service.issue;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.IssueRestClient;
import org.example.webapp.dto.forum.issue.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRestClient issueRestClient;

    @Override
    public List<IssueDto> getIssues() {
        GetIssuesApiDto request = GetIssuesApiDto
                .builder()
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        return issueRestClient.getIssues(request);
    }

    @Override
    public IssueDto getById(Long id) {
        GetIssueByIdApiDto request = GetIssueByIdApiDto.builder()
                .issueId(id)
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        return issueRestClient.getById(request);
    }

    @Override
    public void create(CreateIssueDto issue) {
        CreateIssueApiDto request = CreateIssueApiDto.builder()
                .author(SecurityContextHolder.getContext().getAuthentication().getName())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .build();
        issueRestClient.create(request);
    }

    @Override
    public void delete(DeleteIssueDto issue) {
        DeleteIssueApiDto request = DeleteIssueApiDto.builder()
                .issueId(issue.getIssueId())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        issueRestClient.delete(request);
    }
}
