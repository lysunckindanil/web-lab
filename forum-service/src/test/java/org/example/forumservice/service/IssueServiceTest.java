package org.example.forumservice.service;

import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
import org.example.forumservice.entity.Issue;
import org.example.forumservice.entity.Role;
import org.example.forumservice.entity.User;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.repo.RoleRepository;
import org.example.forumservice.repo.UserRepository;
import org.example.forumservice.util.BadRequestException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
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
class IssueServiceTest {
    private IssueService issueService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @BeforeAll
    public void setUp() {
        commentService = Mockito.mock(CommentService.class);
        issueService = new IssueService(issueRepository, commentService);
        issueService.setUserService(userService);
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
        commentService = Mockito.mock(CommentService.class);
        issueService = new IssueService(issueRepository, commentService);
        issueService.setUserService(userService);
        issueRepository.deleteAll();
    }

    @Test
    void getById() {
        Issue issue = new Issue();
        issueRepository.save(issue);
        Long id = issue.getId();
        Assertions.assertTrue(issueService.getById(id).isPresent());
    }

    @Test
    void getAll() {
        Issue issue = new Issue();
        issueRepository.save(issue);
        Assertions.assertEquals(1, issueService.getAll().size());
    }

    @Test
    void createIssue_AuthorDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> issueService.createIssue(CreateIssueDto.builder().
                author("user1")
                .build()));
    }

    @Test
    void createIssue_AuthorExists_Creates() {
        issueService.createIssue(CreateIssueDto.builder().author("user").build());
        Assertions.assertEquals(1, issueService.getAll().size());
    }


    @Test
    void deleteIssue_IssueDoesntExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> issueService.deleteIssue(DeleteIssueDto.builder()
                .username(userRepository.findByUsername("user").get().getUsername())
                .issueId(1L)
                .build()));
    }

    @Test
    void deleteIssue_UserDoesntExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> issueService.deleteIssue(DeleteIssueDto.builder()
                .username("user1")
                .issueId(issueRepository.save(new Issue()).getId())
                .build()));
    }

    @Test
    void deleteIssue_PretenderIsAuthor_Deletes() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername("user").get());
        issueRepository.save(issue);
        issueService.deleteIssue(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build());
        Mockito.verify(commentService, Mockito.times(1)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void deleteIssue_PretenderIsRedactor_Deletes() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername("user").get());
        issueRepository.save(issue);
        issueService.deleteIssue(DeleteIssueDto.builder()
                .username("redactor")
                .issueId(issue.getId())
                .build());
        Mockito.verify(commentService, Mockito.times(1)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void deleteIssue_PretenderIsAdmin_Deletes() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername("user").get());
        issueRepository.save(issue);
        issueService.deleteIssue(DeleteIssueDto.builder()
                .username("admin")
                .issueId(issue.getId())
                .build());
        Mockito.verify(commentService, Mockito.times(1)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void deleteIssue_PretenderIsNotAuthorOrRedactorOrAdmin_ThrowsException() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername("redactor").get());
        issueRepository.save(issue);
        Assertions.assertThrows(BadRequestException.class, () -> issueService.deleteIssue(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build()));
        Mockito.verify(commentService, Mockito.times(0)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(1, issueRepository.count());
    }

}