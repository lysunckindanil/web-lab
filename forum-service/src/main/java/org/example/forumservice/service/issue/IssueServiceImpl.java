package org.example.forumservice.service.issue;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.issue.*;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.service.user.UserService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final UserService userService;


    @Override
    public IssueDto findById(GetIssueByIdDto dto) {
        return toIssueDto(issueRepository.findById(dto.getIssueId()).get(), dto.getUsername());
    }

    @Override
    public List<IssueDto> findAll(GetIssuesDto dto) {
        return toIssueDtoAll(issueRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")), dto.getUsername());
    }

    @Transactional
    @Override
    public void create(CreateIssueDto dto) {
        User author = userService.findByUsername(dto.getAuthor()).get();

        Issue issue = new Issue();
        issue.setAuthor(author);
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issueRepository.save(issue);
    }

    @Transactional
    @Override
    public void delete(DeleteIssueDto dto) {
        Issue issue = issueRepository.findById(dto.getIssueId()).get();
        if (canDelete(issue, dto.getUsername())) {
            issueRepository.delete(issue);
        } else {
            throw new BadRequestException("Forbidden to delete issue");
        }
    }

    private boolean canDelete(Issue issue, String whoTries) {
        User user = userService.findByUsername(whoTries).get();
        if (userService.isRedactor(user.getUsername())) return true;
        return issue.getAuthor().equals(user);
    }

    private List<IssueDto> toIssueDtoAll(List<Issue> issues, String username) {
        boolean isRedactor = userService.isRedactor(username);
        return issues.stream().map(issue ->
                IssueDto.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .description(issue.getDescription())
                        .createdAt(issue.getCreatedAt())
                        .authorUsername(issue.getAuthor().getUsername())
                        .canDelete(isRedactor || username.equals(issue.getAuthor().getUsername()))
                        .build()
        ).toList();
    }

    private IssueDto toIssueDto(Issue issue, String username) {
        boolean isRedactor = userService.isRedactor(username);
        return
                IssueDto.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .description(issue.getDescription())
                        .createdAt(issue.getCreatedAt())
                        .authorUsername(issue.getAuthor().getUsername())
                        .canDelete(isRedactor || username.equals(issue.getAuthor().getUsername()))
                        .build();
    }
}
