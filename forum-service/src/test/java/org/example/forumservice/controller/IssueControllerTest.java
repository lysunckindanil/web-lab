package org.example.forumservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.forumservice.dto.issue.CreateIssueDto;
import org.example.forumservice.dto.issue.DeleteIssueDto;
import org.example.forumservice.model.Issue;
import org.example.forumservice.model.Role;
import org.example.forumservice.model.User;
import org.example.forumservice.repo.IssueRepository;
import org.example.forumservice.repo.RoleRepository;
import org.example.forumservice.repo.UserRepository;
import org.example.forumservice.service.issue.IssueService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
class IssueControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private IssueService issueService;
    @Autowired
    public IssueRepository issueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeAll
    void setUp() {
        if (userRepository.findByUsername("user").isEmpty()) {
            Role role = new Role("ROLE_USER");
            roleRepository.save(role);
            User user = new User();
            user.setUsername("user");
            user.setPassword("secret");
            userRepository.save(user);
        }
    }

    @AfterEach
    public void tearDown() {
        issueRepository.deleteAll();
    }

    @BeforeEach
    void cleanUp() {
        issueRepository.deleteAll();
    }

    @Test
    void getIssues() throws Exception {
        Mockito.doReturn(new ArrayList<>()).when(issueService).findAll();
        mvc.perform(
                get("/api/v1/issue")
        ).andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(issueService, Mockito.times(1)).findAll();
    }

    @Test
    void create_UserNotExists_ThrowsException() throws Exception {
        CreateIssueDto dto = CreateIssueDto.builder()
                .title("title")
                .description("description")
                .author("user1").build();
        mvc.perform(
                post("/api/v1/issue/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void create_UserExists_ServiceCalled() throws Exception {
        Mockito.doNothing().when(issueService).create(Mockito.any());
        CreateIssueDto dto = CreateIssueDto.builder()
                .title("title")
                .description("description")
                .author("user")
                .build();
        mvc.perform(
                post("/api/v1/issue/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(issueService, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void delete_UserNotExists_ThrowsException() throws Exception {
        DeleteIssueDto dto = DeleteIssueDto.builder()
                .issueId(getIssue("user").getId())
                .username("user1").build();
        mvc.perform(
                post("/api/v1/issue/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void delete_IssueNotExists_ThrowsException() throws Exception {
        DeleteIssueDto dto = DeleteIssueDto.builder()
                .issueId(1L)
                .username("user").build();
        mvc.perform(
                post("/api/v1/issue/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void delete_IssueAndUserExist_ServiceCalled() throws Exception {
        Mockito.doNothing().when(issueService).delete(Mockito.any());
        DeleteIssueDto dto = DeleteIssueDto.builder()
                .issueId(getIssue("user").getId())
                .username("user").build();
        mvc.perform(
                post("/api/v1/issue/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(issueService, Mockito.times(1)).delete(Mockito.any());

    }

    public Issue getIssue(String name) {
        Issue issue = new Issue();
        issue.setAuthor(userRepository.findByUsername(name).get());
        issue.setTitle("title");
        issue.setDescription("description");
        return issueRepository.save(issue);
    }
}