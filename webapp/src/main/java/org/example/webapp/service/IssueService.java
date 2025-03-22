package org.example.webapp.service;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.issue.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService {

    public List<IssueDto> getIssues() {
        IssueDto issueDto = new IssueDto(1L, "title1", "description1", LocalDateTime.now(), "user1");
        IssueDto issueDto1 = new IssueDto(2L, "title2", "description2", LocalDateTime.now(), "user2");
        IssueDto issueDto2 = new IssueDto(3L, "title3", "description3", LocalDateTime.now(), "user3");
        IssueDto issueDto3 = new IssueDto(4L, "title4", "description4", LocalDateTime.now(), "user4");
        return List.of(issueDto, issueDto1, issueDto2, issueDto3);
    }

    public IssueDto getById(Long id) {
        GetIssueByIdApiDto getByIdDto = GetIssueByIdApiDto.builder().issueId(id).build();

        return new IssueDto(1L, "title1", "description1", LocalDateTime.now(), "user1");
    }

    public void create(CreateIssueDto issue) {
        CreateIssueApiDto request = CreateIssueApiDto.builder()
                .author(SecurityContextHolder.getContext().getAuthentication().getName())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .build();
    }

    public void delete(DeleteIssueDto issue) {
        DeleteIssueApiDto request = DeleteIssueApiDto.builder()
                .issueId(issue.getIssueId())
                .username(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
    }
}
