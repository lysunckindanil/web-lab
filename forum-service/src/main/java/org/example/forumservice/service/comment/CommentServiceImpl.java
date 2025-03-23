package org.example.forumservice.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetCommentsByIssueDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.CommentRepository;
import org.example.forumservice.service.issue.IssueService;
import org.example.forumservice.service.user.UserService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private IssueService issueService;


    @Override
    public List<Comment> getByIssue(GetCommentsByIssueDto dto) {
        return commentRepository.getByIssue(issueService.findById(dto.getIssueId()).get());
    }

    @Transactional
    @Override
    public void create(CreateCommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());

        Issue issue = issueService.findById(dto.getIssueId()).get();
        User user = userService.findByUsername(dto.getAuthorUsername()).get();

        comment.setIssue(issue);
        comment.setAuthor(user);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void delete(DeleteCommentDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentId()).get();
        User user = userService.findByUsername(dto.getUsername()).get();

        if (comment.getAuthor().equals(user)) {
            commentRepository.delete(comment);
        } else if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"))) {
            commentRepository.delete(comment);
        } else {
            throw new BadRequestException("Forbidden to delete comment");
        }
    }

    @Transactional
    @Override
    public void deleteAllByIssue(Long issueId) {
        Issue issue = issueService.findById(issueId).get();
        commentRepository.deleteAll(commentRepository.getByIssue(issue));
    }


    @Autowired
    @Override
    public void setIssueService(@Lazy IssueService issueService) {
        this.issueService = issueService;
    }
}
