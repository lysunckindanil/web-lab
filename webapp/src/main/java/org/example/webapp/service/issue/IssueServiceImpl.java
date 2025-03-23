package org.example.webapp.service.issue;

import lombok.RequiredArgsConstructor;
import org.example.webapp.client.IssueRestClient;
import org.example.webapp.dto.forum.issue.*;
import org.example.webapp.model.User;
import org.example.webapp.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRestClient issueRestClient;
    private final UserService userService;

    @Override
    public List<IssueDto> getIssues() {
        return validateAll(issueRestClient.getIssues());
    }

    @Override
    public IssueDto getById(Long id) {
        GetIssueByIdApiDto getByIdDto = GetIssueByIdApiDto.builder().issueId(id).build();
        return validate(issueRestClient.getById(getByIdDto));
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

    private IssueDto validate(IssueDto issueDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.loadUserByUsername(username);
        if (user == null) return null;

        if (user.hasRole("ROLE_REDACTOR")) issueDto.setCanDelete(true);
        else if (issueDto.getAuthorUsername().equals(username)) issueDto.setCanDelete(true);

        return issueDto;
    }

    private List<IssueDto> validateAll(List<IssueDto> issues) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.loadUserByUsername(username);
        if (user == null) return null;

        if (user.hasRole("ROLE_REDACTOR")) {
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
