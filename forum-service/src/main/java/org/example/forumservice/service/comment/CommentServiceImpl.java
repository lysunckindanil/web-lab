package org.example.forumservice.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.dto.comment.CommentDto;
import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetCommentsByIssueDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.CommentRepository;
import org.example.forumservice.service.issue.FindIssueById;
import org.example.forumservice.service.user.UserService;
import org.example.forumservice.util.BadRequestException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ObjectProvider<FindIssueById> getIssueById;


    @Override
    public List<CommentDto> getByIssue(GetCommentsByIssueDto dto) {
        return toCommentDtoAll(commentRepository.getByIssueId(dto.getIssueId()), dto.getUsername());
    }

    @Transactional
    @Override
    public void create(CreateCommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());

        Issue issue = getIssueById.getObject().find(dto.getIssueId());
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

    private boolean canDelete(Comment comment, String username) {
        if (userService.isRedactor(username)) return true;
        return comment.getAuthor().getUsername().equals(username);
    }

    private List<CommentDto> toCommentDtoAll(List<Comment> comments, String username) {
        boolean isRedactor = userService.isRedactor(username);

        return comments.stream().map(comment ->
                CommentDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .authorUsername(comment.getAuthor().getUsername())
                        .canDelete(isRedactor || username.equals(comment.getAuthor().getUsername()))
                        .build()
        ).toList();
    }
}
