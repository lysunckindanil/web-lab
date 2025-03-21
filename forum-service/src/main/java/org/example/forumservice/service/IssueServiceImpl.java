package org.example.forumservice.service;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.entity.Role;
import org.example.forumservice.util.BadRequestException;
import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
import org.example.forumservice.entity.Issue;
import org.example.forumservice.entity.User;
import org.example.forumservice.repo.IssueRepository;
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
        Optional<User> userOptional = userService.findByUsername(dto.getAuthor());
        if (userOptional.isEmpty()) throw new BadRequestException("Author doesn't exist");

        Issue issue = new Issue();
        issue.setAuthor(userOptional.get());
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issueRepository.save(issue);
    }

    @Transactional
    @Override
    public void delete(DeleteIssueDto dto) {
        Optional<Issue> issueOptional = issueRepository.findById(dto.getIssueId());
        if (issueOptional.isEmpty()) throw new BadRequestException("Issue doesn't exist");
        Optional<User> userOptional = userService.findByUsername(dto.getUsername());
        if (userOptional.isEmpty()) throw new BadRequestException("Author doesn't exist");
        User user = userOptional.get();

        if (user.getUsername().equals(issueOptional.get().getAuthor().getUsername())) {
            commentService.deleteAllByIssue(issueOptional.get().getId());
            issueRepository.delete(issueOptional.get());
        } else if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"))) {
            commentService.deleteAllByIssue(issueOptional.get().getId());
            issueRepository.delete(issueOptional.get());
        } else {
            throw new BadRequestException("Forbidden to delete issue");
        }
    }

    @Autowired
    public void setUserService(@Lazy UserService userService) {
        this.userService = userService;
    }
}
