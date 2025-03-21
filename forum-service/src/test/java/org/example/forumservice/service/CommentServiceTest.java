package org.example.forumservice.service;

import org.example.forumservice.dto.comment.CreateCommentDto;
import org.example.forumservice.dto.comment.DeleteCommentDto;
import org.example.forumservice.dto.comment.GetByIssueDto;
import org.example.forumservice.entity.Comment;
import org.example.forumservice.entity.Issue;
import org.example.forumservice.entity.Role;
import org.example.forumservice.entity.User;
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
        Issue issue = new Issue();
        issueRepository.save(issue);
        Comment comment = new Comment();
        comment.setIssue(issue);
        commentRepository.save(comment);
        Comment comment1 = new Comment();
        comment1.setIssue(issue);
        commentRepository.save(comment1);
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
    void createComment_IssueAndAuthorExist_Creates() {
        Issue issue = new Issue();
        issueRepository.save(issue);
        User user = userRepository.findByUsername("user").get();

        commentService.createComment(CreateCommentDto.builder()
                .authorUsername(user.getUsername())
                .issueId(issue.getId())
                .build());
        Assertions.assertEquals(1, commentRepository.count());
    }

    @Test
    void createComment_IssueNotFound_ThrowsException() {
        User user = userRepository.findByUsername("user").get();

        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.createComment(CreateCommentDto.builder()
                        .issueId(1L)
                        .authorUsername(user.getUsername())
                        .build()));
    }

    @Test
    void createComment_UserNotFound_ThrowsException() {
        System.out.println(userRepository.count());
        Issue issue = new Issue();
        issueRepository.save(issue);

        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.createComment(CreateCommentDto.builder()
                        .issueId(issue.getId())
                        .build()));
    }

    @Test
    void deleteComment_PretenderIsAuthor_Deletes() {
        User user = userRepository.findByUsername("user").get();
        userRepository.save(user);
        Comment comment = new Comment();
        comment.setAuthor(user);
        commentRepository.save(comment);
        commentService.deleteComment(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("user").build());
        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void deleteComment_PretenderIsRedactor_Deletes() {
        User user = userRepository.findByUsername("user").get();
        Comment comment = new Comment();
        comment.setAuthor(user);
        commentRepository.save(comment);

        commentService.deleteComment(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("redactor")
                .build());
        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void deleteComment_PretenderIsAdmin_Deletes() {
        User user = userRepository.findByUsername("user").get();
        Comment comment = new Comment();
        comment.setAuthor(user);
        commentRepository.save(comment);

        commentService.deleteComment(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("admin")
                .build());
        Assertions.assertEquals(0, commentRepository.count());
    }

    @Test
    void deleteComment_PretenderIsNotAuthorOrRedactorOrAdmin_ThrowsException() {
        User user = userRepository.findByUsername("redactor").get();
        Comment comment = new Comment();
        comment.setAuthor(user);
        commentRepository.save(comment);

        Assertions.assertThrows(BadRequestException.class, () -> commentService.deleteComment(DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username("user")
                .build()));
        Assertions.assertEquals(1, commentRepository.count());
    }


    @Test
    void deleteComment_UserNotFound_ThrowsException() {
        Comment comment = new Comment();
        commentRepository.save(comment);
        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.deleteComment(DeleteCommentDto.builder()
                        .commentId(comment.getId())
                        .username("user1")
                        .build()));
    }

    @Test
    void deleteComment_CommentNotFound_ThrowsException() {
        User user = userRepository.findByUsername("user").get();
        userRepository.save(user);
        Assertions.assertThrows(BadRequestException.class,
                () -> commentService.deleteComment(DeleteCommentDto.builder()
                        .commentId(1L)
                        .username("user")
                        .build()));
    }


    @Test
    void deleteAllByIssue() {
        Issue issue = new Issue();
        issueRepository.save(issue);
        Comment comment = new Comment();
        comment.setIssue(issue);
        commentRepository.save(comment);
        Comment comment1 = new Comment();
        comment1.setIssue(issue);
        commentRepository.save(comment1);
        commentService.deleteAllByIssue(issue.getId());
        Assertions.assertEquals(0, commentRepository.count());
    }
}