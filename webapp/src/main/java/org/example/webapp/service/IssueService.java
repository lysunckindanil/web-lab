package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.CreateIssueApiDto;
import org.example.webapp.dto.forum.CreateIssueDto;
import org.example.webapp.dto.forum.DeleteIssueApiDto;
import org.example.webapp.dto.forum.DeleteIssueDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {
    public void create(CreateIssueDto issue, String author) {
        CreateIssueApiDto dto = CreateIssueApiDto.builder()
                .author(author)
                .title(issue.getTitle())
                .description(issue.getDescription())
                .build();
        System.out.println(issue.getDescription());
        System.out.println(issue.getTitle());
    }

    public void delete(DeleteIssueDto issue, String username) {
        DeleteIssueApiDto dto = DeleteIssueApiDto.builder()
                .issueId(issue.getIssueId())
                .username(username)
                .build();
    }
}
