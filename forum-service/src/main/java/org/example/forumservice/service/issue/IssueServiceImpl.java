package org.example.forumservice.service.issue;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.issue.*;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.service.comment.CommentService;
import org.example.forumservice.service.user.UserService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final CommentService commentService;
    private UserService userService;


    @Override
    public IssueDto findById(GetIssueByIdDto dto) {
        return toIssueDto(issueRepository.findById(dto.getIssueId()).get(), dto.getUsername());
    }

    @Override
    public Optional<Issue> findById(Long issueId) {
        return issueRepository.findById(issueId);
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
            commentService.deleteAllByIssue(issue.getId());
            issueRepository.delete(issue);
        } else {
            throw new BadRequestException("Forbidden to delete issue");
        }
    }

    private boolean canDelete(Issue issue, String whoTries) {
        User user = userService.findByUsername(whoTries).get();

        if (issue.getAuthor().equals(user))
            return true;

        if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN")))
            return true;

        return false;
    }

    private List<IssueDto> toIssueDtoAll(List<Issue> issues, String username) {
        User user = userService.findByUsername(username).get();

        List<IssueDto> issueDtos = new ArrayList<>();
        if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"))) {
            for (Issue issue : issues) {
                issueDtos.add(
                        IssueDto.builder()
                                .id(issue.getId())
                                .title(issue.getTitle())
                                .description(issue.getDescription())
                                .createdAt(issue.getCreatedAt())
                                .authorUsername(issue.getAuthor().getUsername())
                                .canDelete(true)
                                .build()
                );
            }

        } else {
            for (Issue issue : issues) {
                String author = issue.getAuthor().getUsername();
                issueDtos.add(
                        IssueDto.builder()
                                .id(issue.getId())
                                .title(issue.getTitle())
                                .description(issue.getDescription())
                                .createdAt(issue.getCreatedAt())
                                .authorUsername(issue.getAuthor().getUsername())
                                .canDelete(author.equals(username))
                                .build()
                );
            }
        }
        return issueDtos;
    }

    private IssueDto toIssueDto(Issue issue, String username) {
        User user = userService.findByUsername(username).get();
        boolean canDelete = false;
        if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN")))
            canDelete = true;
        if (issue.getAuthor().getUsername().equals(username))
            canDelete = true;

        return
                IssueDto.builder()
                        .id(issue.getId())
                        .title(issue.getTitle())
                        .description(issue.getDescription())
                        .createdAt(issue.getCreatedAt())
                        .authorUsername(issue.getAuthor().getUsername())
                        .canDelete(canDelete)
                        .build();
    }

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }
}
