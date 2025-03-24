package org.example.forumservice.service;

import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
import org.example.forumservice.dto.issue.GetIssueByIdDto;
import org.example.forumservice.dto.issue.GetIssuesDto;
import org.example.forumservice.model.Comment;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.CommentRepository;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.repo.RoleRepository;
import org.example.forumservice.repo.UserRepository;
import org.example.forumservice.service.issue.IssueService;
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
class IssueServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private IssueService issueService;


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
    void findById() {
        Issue issue = getIssue("user");
        GetIssueByIdDto dto = GetIssueByIdDto.builder()
                .issueId(issue.getId())
                .username("user")
                .build();
        Assertions.assertNotNull(issueService.findById(dto));
        Assertions.assertEquals(issue.getId(), issueService.findById(dto).getId());
    }

    @Test
    void findById_CanDeleteOnlyAuthor() {
        Issue issue_user = getIssue("user");
        Issue issue_admin = getIssue("admin");
        Assertions.assertTrue(issueService.findById(
                GetIssueByIdDto.builder()
                        .username("user")
                        .issueId(issue_user.getId()).build()
        ).getCanDelete());
        Assertions.assertFalse(issueService.findById(
                GetIssueByIdDto.builder()
                        .username("user")
                        .issueId(issue_admin.getId()).build()
        ).getCanDelete());
    }

    @Test
    void findById_RedactorCanDeleteEverything() {
        Issue issue_user = getIssue("user");
        Issue issue_admin = getIssue("admin");
        Assertions.assertTrue(issueService.findById(
                GetIssueByIdDto.builder()
                        .username("admin")
                        .issueId(issue_user.getId()).build()
        ).getCanDelete());
        Assertions.assertTrue(issueService.findById(
                GetIssueByIdDto.builder()
                        .username("admin")
                        .issueId(issue_admin.getId()).build()
        ).getCanDelete());
    }


    @Test
    void findAll() {
        GetIssuesDto dto = GetIssuesDto.builder().username("user").build();
        getIssue("user");
        Assertions.assertEquals(1, issueService.findAll(dto).size());
    }

    @Test
    void findAll_CanDeleteOnlyAuthor() {
        GetIssuesDto dto = GetIssuesDto.builder().username("user").build();
        getIssue("user");
        getIssue("admin");
        Assertions.assertEquals(2, issueService.findAll(dto).size());
        issueService.findAll(dto).forEach(
                issue -> {
                    if (issue.getAuthorUsername().equals("admin"))
                        Assertions.assertFalse(issue.getCanDelete());
                    else
                        Assertions.assertTrue(issue.getCanDelete());
                }
        );
    }

    @Test
    void findAll_RedactorCanDeleteAll() {
        GetIssuesDto dto = GetIssuesDto.builder().username("admin").build();
        getIssue("user");
        getIssue("admin");
        Assertions.assertEquals(2, issueService.findAll(dto).size());
        issueService.findAll(dto).forEach(
                issue -> Assertions.assertTrue(issue.getCanDelete())
        );
    }


    @Test
    void create_CorrectData_Creates() {
        GetIssuesDto dto = GetIssuesDto.builder().username("user").build();
        issueService.create(CreateIssueDto.builder().author("user").title("title").description("description").build());
        Assertions.assertEquals(1, issueService.findAll(dto).size());
    }


    @Test
    void delete_PretenderIsAuthor_Deletes() {
        Issue issue = getIssue("user");
        issueService.delete(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build());
        Assertions.assertEquals(0, issueRepository.count());
    }


    @Test
    void delete_PretenderIsRedactor_Deletes() {
        Issue issue = getIssue("user");
        issueService.delete(DeleteIssueDto.builder()
                .username("redactor")
                .issueId(issue.getId())
                .build());
        Assertions.assertEquals(0, issueRepository.count());
    }

    @Test
    void delete_PretenderIsAdmin_Deletes() {
        Issue issue = getIssue("user");
        issueService.delete(DeleteIssueDto.builder()
                .username("admin")
                .issueId(issue.getId())
                .build());
        Assertions.assertEquals(0, issueRepository.count());
    }


    @Test
    void delete_PretenderIsNotAuthorOrRedactorOrAdmin_ThrowsException() {
        Issue issue = getIssue("redactor");
        Assertions.assertThrows(BadRequestException.class, () -> issueService.delete(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build()));
        Assertions.assertEquals(1, issueRepository.count());
    }


    @Test
    void delete_IssueWithComments_DeletesComments() {
        Issue issue = getIssue("user");
        Comment comment = getComment(issue, "user");
        Comment comment1 = getComment(issue, "user");
        issue.getComments().add(comment);
        issue.getComments().add(comment1);
        issueRepository.save(issue);

        issueService.delete(DeleteIssueDto.builder()
                .username("user")
                .issueId(issue.getId())
                .build());

        Assertions.assertEquals(0, issueRepository.count());
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