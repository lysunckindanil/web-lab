package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.IssueRestClient;
import org.example.webapp.dto.forum.issue.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRestClient issueRestClient;
    private final UserDetailsService userDetailsService;

    public List<IssueDto> getIssues() {
        return validateAll(issueRestClient.getIssues());
    }

    public IssueDto getById(Long id) {
        GetIssueByIdApiDto getByIdDto = GetIssueByIdApiDto.builder().issueId(id).build();
        return validate(issueRestClient.getById(getByIdDto));
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

    private IssueDto validate(IssueDto issueDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userDetailsService.loadUserByUsername(username)
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_REDACTOR"))
        ) issueDto.setCanDelete(true);
        else if (issueDto.getAuthorUsername().equals(username)) issueDto.setCanDelete(true);
        return issueDto;
    }

    private List<IssueDto> validateAll(List<IssueDto> issues) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userDetailsService.loadUserByUsername(username)
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_REDACTOR"))) {
            for (IssueDto issue : issues) {
                issue.setCanDelete(true);

            }
            return issues;
        }

        for (IssueDto issue : issues) {
            if (issue.getAuthorUsername().equals(username)) issue.setCanDelete(true);
        }
        return issues;
    }
}
