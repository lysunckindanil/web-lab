package org.example.forumservice.service;

import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetByIssueDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.CommentRepository;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.repo.RoleRepository;
import org.example.forumservice.repo.UserRepository;
import org.example.forumservice.util.BadRequestException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CommentServiceTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private IssueRepository issueRepository;

    @BeforeAll
    public void setUp() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("secret");
        userRepository.save(user);

        Role role = new Role("ROLE_REDACTOR");
        roleRepository.save(role);
        User redactor = new User();
        redactor.setUsername("redactor");
        redactor.setPassword("secret");
        redactor.setRoles(List.of(role));
        userRepository.save(redactor);

        Role role2 = new Role("ROLE_ADMIN");
        roleRepository.save(role2);
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("secret");
        admin.setRoles(List.of(role2));
        userRepository.save(admin);
    }

    @BeforeEach
    public void cleanUp() {
        commentRepository.deleteAll();
        issueRepository.deleteAll();
    }


    @Test
    void getByIssue() {
        Issue issue = getIssue("user");
        getComment(issue, "user");
        getComment(issue, "user");

        Assertions.assertEquals(2, commentService.getByIssue(GetByIssueDto
                        .builder()
                        .issueId(issue.getId())
                        .build())
                .size());
    }

    @Test
    void getByIssue_IssueNotFound_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> commentService.getByIssue(GetByIssueDto
                        .builder()
                        .issueId(1L)
                        .build())
                .size());
    }

    @Test
    void create_IssueAndAuthorExist_Creates() {
        User user = userRepository.findByUsername("user").get();

        commentService.create(CreateCommentDto.builder()
                .content("content")
                .authorUsername(user.getUsername())
                .issueId(getIssue("user").getId())
                .build());
        Assertions.assertEquals(1, commentRepository.count());
    }

    @Test
    void create_IssueNotFound_ThrowsException() {
        User user = userRepository.findByUsername("user").get();

        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.create(CreateCommentDto.builder()
                        .issueId(1L)
                        .authorUsername(user.getUsername())
                        .build()));
    }

    @Test
    void create_UserNotFound_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.create(CreateCommentDto.builder()
                        .issueId(getIssue("user").getId())
                        .build()));
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


    @Test
    void delete_UserNotFound_ThrowsException() {
        Issue issue = getIssue("user");
        Comment comment = getComment(issue, "user");

        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.delete(DeleteCommentDto.builder()
                        .commentId(comment.getId())
                        .username("user1")
                        .build()));
    }

    @Test
    void deleteComment_NotFound_ThrowsException() {
        User user = userRepository.findByUsername("user").get();
        userRepository.save(user);
        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.delete(DeleteCommentDto.builder()
                        .commentId(1L)
                        .username("user")
                        .build()));
    }


    @Test
    void deleteAllByIssue() {
        Issue issue = getIssue("user");
        getComment(issue, "user");
        getComment(issue, "user");
        commentService.deleteAllByIssue(issue.getId());
        Assertions.assertEquals(0, commentRepository.count());
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