package org.example.forumservice.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.comment.CommentDto;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private IssueService issueService;


    @Override
    public List<CommentDto> getByIssue(GetCommentsByIssueDto dto) {
        return toCommentDtoAll(commentRepository.getByIssueId(dto.getIssueId()), dto.getUsername());
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

        if (canDelete(comment, dto.getUsername())) {
            commentRepository.delete(comment);
        } else {
            throw new BadRequestException("Forbidden to delete comment");
        }
    }

    @Transactional
    @Override
    public void deleteAllByIssue(Long issueId) {
        commentRepository.deleteAll(commentRepository.getByIssueId(issueId));
    }

    private boolean canDelete(Comment comment, String whoTries) {
        User user = userService.findByUsername(whoTries).get();

        if (comment.getAuthor().equals(user))
            return true;

        if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN")))
            return true;

        return false;
    }

    private List<CommentDto> toCommentDtoAll(List<Comment> comments, String username) {
        User user = userService.findByUsername(username).get();

        List<CommentDto> commentDtos = new ArrayList<>();
        if (user.getRoles().stream().map(Role::getName).anyMatch(role -> role.equals("ROLE_REDACTOR") || role.equals("ROLE_ADMIN"))) {
            for (Comment comment : comments) {
                commentDtos.add(
                        CommentDto.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .createdAt(comment.getCreatedAt())
                                .authorUsername(comment.getAuthor().getUsername())
                                .canDelete(true)
                                .build()
                );
            }

        } else {
            for (Comment comment : comments) {
                String authorUsername = comment.getAuthor().getUsername();
                commentDtos.add(
                        CommentDto.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .createdAt(comment.getCreatedAt())
                                .authorUsername(authorUsername)
                                .canDelete(authorUsername.equals(username))
                                .build()
                );
            }
        }
        return commentDtos;
    }

    @Autowired
    @Override
    public void setIssueService(@Lazy IssueService issueService) {
        this.issueService = issueService;
    }
}
