package org.example.webapp.client;

import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.comment.CommentDto;
import org.example.webapp.dto.forum.comment.CreateCommentApiDto;
import org.example.webapp.dto.forum.comment.DeleteCommentApiDto;
import org.example.webapp.dto.forum.comment.GetCommentsByIssueApiDto;
import org.example.webapp.util.BadRequestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class CommentRestClientImpl implements CommentRestClient {
    private final RestClient restClient;
    private static final ParameterizedTypeReference<List<CommentDto>> COMMENT_LIST_TYPE = new ParameterizedTypeReference<>() {
    };

    @Override
    public List<CommentDto> getByIssue(GetCommentsByIssueApiDto dto) {
        try {
            return restClient
                    .post()
                    .uri("/getByIssue")
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(COMMENT_LIST_TYPE);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(extractError(exception));
        }
    }

    @Override
    public void create(CreateCommentApiDto dto) {
        try {
            restClient
                    .post()
                    .uri("/create")
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(extractError(exception));
        }
    }

    @Override
    public void delete(DeleteCommentApiDto dto) {
        try {
            restClient
                    .post()
                    .uri("/delete")
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(extractError(exception));
        }
    }

    private static String extractError(HttpClientErrorException.BadRequest exception) {
        ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        String error = exception.getMessage();
        if (problemDetail != null && problemDetail.getProperties() != null && problemDetail.getProperties().containsKey("error")) {
            error = problemDetail.getProperties().get("error").toString();
        }
        return error;
    }
}
