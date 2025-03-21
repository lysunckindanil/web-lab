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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IssueServiceTest {
    private IssueServiceImpl issueService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private UserService userService;

    @BeforeAll
    public void setUp() {
        commentService = Mockito.mock(CommentServiceImpl.class);
        issueService = new IssueServiceImpl(issueRepository, commentService);
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
        commentService = Mockito.mock(CommentServiceImpl.class);
        issueService = new IssueServiceImpl(issueRepository, commentService);
        issueService.setUserService(userService);
        issueRepository.deleteAll();
    }

    @Test
    void findById() {
        Issue issue = getIssue("user");
        Assertions.assertTrue(issueService.findById(issue.getId()).isPresent());
    }

    @Test
    void findAll() {
        getIssue("user");
        Assertions.assertEquals(1, issueService.findAll().size());
    }

    @Test
    void create_AuthorDoesNotExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> issueService.create(CreateIssueDto.builder()
                .author("user1")
                .title("title")
                .description("description")
                .build()));
    }

    @Test
    void create_AuthorExists_Creates() {
        issueService.create(CreateIssueDto.builder().author("user").title("title").description("description").build());
        Assertions.assertEquals(1, issueService.findAll().size());
    }


    @Test
    void deleteIssue_DoesntExist_ThrowsException() {
        Assertions.assertThrows(BadRequestException.class, () -> issueService.delete(DeleteIssueDto.builder()
                .username(userRepository.findByUsername("user").get().getUsername())
                .issueId(1L)
                .build()));
    }

    @Test
    void delete_UserDoesntExist_ThrowsException() {
        Issue issue = getIssue("user");
        Assertions.assertThrows(BadRequestException.class, () -> issueService.delete(DeleteIssueDto.builder()
                .username("user1")
                .issueId(issue.getId())
                .build()));
    }

    @Test
    void delete_PretenderIsAuthor_Deletes() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = getIssue("user");
        issueService.delete(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build());
        Mockito.verify(commentService, Mockito.times(1)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void delete_PretenderIsRedactor_Deletes() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = getIssue("user");
        issueService.delete(DeleteIssueDto.builder()
                .username("redactor")
                .issueId(issue.getId())
                .build());
        Mockito.verify(commentService, Mockito.times(1)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void delete_PretenderIsAdmin_Deletes() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = getIssue("user");
        issueService.delete(DeleteIssueDto.builder()
                .username("admin")
                .issueId(issue.getId())
                .build());
        Mockito.verify(commentService, Mockito.times(1)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void delete_PretenderIsNotAuthorOrRedactorOrAdmin_ThrowsException() {
        Mockito.doNothing().when(commentService).deleteAllByIssue(Mockito.any());

        Issue issue = getIssue("redactor");
        Assertions.assertThrows(BadRequestException.class, () -> issueService.delete(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build()));
        Mockito.verify(commentService, Mockito.times(0)).deleteAllByIssue(Mockito.any());
        Assertions.assertEquals(1, issueRepository.count());
    }

    public Issue getIssue(String name) {
        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername(name).get());
        issue.setTitle("title");
        issue.setDescription("description");
        return issueRepository.save(issue);
    }

}