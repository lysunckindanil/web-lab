package org.example.forumservice.service;

import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetCommentsByIssueDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.CommentRepository;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.repo.RoleRepository;
import org.example.forumservice.repo.UserRepository;
import org.example.forumservice.service.comment.CommentService;
import org.example.forumservice.util.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@ActiveProfiles("test")
@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentServiceTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private CommentService commentService;

    @BeforeAll
    public void setUp() {
        if (userRepository.findByUsername("user").isEmpty()) {
            Role role = new Role("ROLE_USER");
            roleRepository.save(role);
            User user = new User();
            user.setUsername("user");
            user.setPassword("secret");
            userRepository.save(user);
        }

        if (userRepository.findByUsername("redactor").isEmpty()) {
            Role role = new Role("ROLE_REDACTOR");
            roleRepository.save(role);
            User redactor = new User();
            redactor.setUsername("redactor");
            redactor.setPassword("secret");
            redactor.setRoles(List.of(role));
            userRepository.save(redactor);
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            Role role2 = new Role("ROLE_ADMIN");
            roleRepository.save(role2);
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("secret");
            admin.setRoles(List.of(role2));
            userRepository.save(admin);
        }
    }


    @Test
    void getByIssue() {
        Issue issue = getIssue("user");
        getComment(issue, "user");
        getComment(issue, "user");

        Assertions.assertEquals(2, commentService.getByIssue(GetCommentsByIssueDto
                        .builder()
                        .username("user")
                        .issueId(issue.getId())
                        .build())
                .size());
    }

    @Test
    void getByIssue_CanDeleteOnlyIfAuthor() {
        Issue issue = getIssue("user");
        getComment(issue, "user");
        getComment(issue, "admin");


        commentService.getByIssue(GetCommentsByIssueDto
                .builder()
                .username("user")
                .issueId(issue.getId())
                .build()).forEach(
                comment -> {
                    if (comment.getAuthorUsername().equals("admin"))
                        Assertions.assertFalse(comment.getCanDelete());
                    else Assertions.assertTrue(comment.getCanDelete());
                }
        );
    }

    @Test
    void getByIssue_RedactorCanDeleteEverything() {
        Issue issue = getIssue("user");
        getComment(issue, "user");
        getComment(issue, "admin");


        commentService.getByIssue(GetCommentsByIssueDto
                .builder()
                .username("admin")
                .issueId(issue.getId())
                .build()).forEach(
                comment -> Assertions.assertEquals(true, comment.getCanDelete())
        );
    }


    @Test
    void create_CorrectData_Creates() {
        User user = userRepository.findByUsername("user").get();

        commentService.create(CreateCommentDto.builder()
                .content("content")
                .authorUsername(user.getUsername())
                .issueId(getIssue("user").getId())
                .build());
        Assertions.assertEquals(1, commentRepository.count());
    }


    @Test
    void delete_PretenderIsAuthor_Deletes() {
        Comment comment = getComment(getIssue("user"), "user");

        commentRepository.save(comment);
        commentService.delete(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("user").build());
        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void delete_PretenderIsRedactor_Deletes() {
        Comment comment = getComment(getIssue("user"), "user");

        commentService.delete(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("redactor")
                .build());
        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void delete_PretenderIsAdmin_Deletes() {
        Comment comment = getComment(getIssue("user"), "user");

        commentService.delete(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("admin")
                .build());
        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void delete_PretenderIsNotAuthorOrRedactorOrAdmin_ThrowsException() {
        Comment comment = getComment(getIssue("redactor"), "redactor");

        Assertions.assertThrows(BadRequestException.class, () -> commentService.delete(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("user")
                .build()));
        Assertions.assertEquals(1, commentRepository.count());
    }

    public Issue getIssue(String name) {
        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername(name).get());
        issue.setTitle("title");
        issue.setDescription("description");
        return issueRepository.save(issue);
    }

    public Comment getComment(Issue issue, String name) {
        Comment comment = new Comment();
        comment.setAuthor(userRepository.findByUsername(name).get());
        comment.setIssue(issue);
        comment.setContent("content");
        return commentRepository.save(comment);
    }
}