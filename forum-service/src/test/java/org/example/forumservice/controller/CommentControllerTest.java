package org.example.forumservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.example.forumservice.service.comment.CommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private CommentService commentService;

    @Autowired
    public IssueRepository issueRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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
    }


    @AfterEach
    public void beforeEach() {
        commentRepository.deleteAll();
        issueRepository.deleteAll();
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

    @Test
    void getByIssue_IssueDoesntExist_ThrowsException() throws Exception {
        GetByIssueDto dto = GetByIssueDto.builder().issueId(1L).build();
        mvc.perform(
                post("/api/v1/comment/getByIssue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getByIssue_IssueExists_ServiceCalled() throws Exception {
        Mockito.doReturn(new ArrayList<>()).when(commentService).getByIssue(Mockito.any());
        GetByIssueDto dto = GetByIssueDto.builder().issueId(getIssue("user").getId()).build();
        mvc.perform(
                post("/api/v1/comment/getByIssue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(commentService, Mockito.times(1)).getByIssue(Mockito.any());
    }

    @Test
    void create_UserDoesntExist_ThrowsException() throws Exception {
        CreateCommentDto dto = CreateCommentDto.builder()
                .issueId(getIssue("user").getId())
                .authorUsername("user1").build();
        mvc.perform(
                post("/api/v1/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void create_IssueDoesntExist_ThrowsException() throws Exception {
        CreateCommentDto dto = CreateCommentDto.builder()
                .issueId(1L)
                .authorUsername("user").build();
        mvc.perform(
                post("/api/v1/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void create_IssueAndAuthorExist_ServiceCalled() throws Exception {
        Mockito.doNothing().when(commentService).create(Mockito.any());
        Comment comment = getComment(getIssue("user"), "user");
        CreateCommentDto dto = CreateCommentDto.builder().
                issueId(comment.getIssue().getId())
                .authorUsername(comment.getAuthor().getUsername())
                .content(comment.getContent())
                .build();
        mvc.perform(
                post("/api/v1/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(commentService, Mockito.times(1)).create(Mockito.any());
    }

    @Test
    void delete_UserDoesntExist_ThrowsException() throws Exception {
        DeleteCommentDto dto = DeleteCommentDto.builder()
                .commentId(getComment(getIssue("user"), "user").getId())
                .username("user1").build();
        mvc.perform(
                post("/api/v1/comment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void delete_CommentDoesntExist_ThrowsException() throws Exception {
        DeleteCommentDto dto = DeleteCommentDto.builder()
                .commentId(1L)
                .username("user")
                .build();

        mvc.perform(
                post("/api/v1/comment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void delete_CommentAndAuthorExist_ServiceCalled() throws Exception {
        Mockito.doNothing().when(commentService).delete(Mockito.any());
        Comment comment = getComment(getIssue("user"), "user");
        DeleteCommentDto dto = DeleteCommentDto.builder()
                .commentId(comment.getId())
                .username(comment.getAuthor().getUsername())
                .build();

        mvc.perform(
                post("/api/v1/comment/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto))
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(commentService, Mockito.times(1)).delete(Mockito.any());
    }
}