package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.issue.IssueDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {
    private final IssueService issueService;
    private final UserDetailsService userDetailsService;

    public List<IssueDto> getIssues() {
        return issueService.getIssues();
    }

    public boolean isRedactor() {
        return userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_REDACTOR"));
    }
}
