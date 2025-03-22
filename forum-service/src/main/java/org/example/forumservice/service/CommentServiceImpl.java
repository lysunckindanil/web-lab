package org.example.forumservice.service;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetByIssueDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.CommentRepository;
import org.example.forumservice.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private IssueService issueService;


    @Override
    public List<Comment> getByIssue(GetByIssueDto dto) {
        Optional<Issue> issueOptional = issueService.findById(dto.getIssueId());
        if (issueOptional.isEmpty()) throw new BadRequestException("Issue doesn't exist");
        return commentRepository.getByIssue(issueOptional.get());
    }

    @Transactional
    @Override
    public void create(CreateCommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());

        Optional<Issue> issueOptional = issueService.findById(dto.getIssueId());
        if (issueOptional.isEmpty()) throw new BadRequestException("Issue doesn't exist");
        Optional<User> userOptional = userService.findByUsername(dto.getAuthorUsername());
        if (userOptional.isEmpty()) throw new BadRequestException("User doesn't exist");

        comment.setIssue(issueOptional.get());
        comment.setAuthor(userOptional.get());
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void delete(DeleteCommentDto dto) {
        Optional<Comment> commentOptional = commentRepository.findById(dto.getCommentId());
        if (commentOptional.isEmpty()) throw new BadRequestException("Comment doesn't exist");
        Optional<User> userOptional = userService.findByUsername(dto.getUsername());
        if (userOptional.isEmpty()) throw new BadRequestException("User doesn't exist");

        User user = userOptional.get();
        if (commentOptional.get().getAuthor().equals(user)) {
            commentRepository.delete(commentOptional.get());
        } else if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"))) {
            commentRepository.delete(commentOptional.get());
        } else {
            throw new BadRequestException("Forbidden to delete comment");
        }
    }

    @Transactional
    @Override
    public void deleteAllByIssue(Long issueId) {
        Optional<Issue> issueOptional = issueService.findById(issueId);
        if (issueOptional.isEmpty()) throw new BadRequestException("Issue doesn't exist");
        commentRepository.deleteAll(commentRepository.getByIssue(issueOptional.get()));
    }

    @Autowired
    public void setIssueService(@Lazy IssueService issueService) {
        this.issueService = issueService;
    }
}
