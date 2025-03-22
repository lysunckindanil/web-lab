package org.example.forumservice.service.issue;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final CommentService commentService;
    private UserService userService;


    @Override
    public Optional<Issue> findById(Long id) {
        return issueRepository.findById(id);
    }

    @Override
    public List<Issue> findAll() {
        return issueRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
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
        User user = userService.findByUsername(dto.getUsername()).get();

        if (user.getUsername().equals(issue.getAuthor().getUsername())) {
            commentService.deleteAllByIssue(issue.getId());
            issueRepository.delete(issue);
        } else if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"))) {
            commentService.deleteAllByIssue(issue.getId());
            issueRepository.delete(issue);
        } else {
            throw new BadRequestException("Forbidden to delete issue");
        }
    }

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }
}
