package org.example.webapp.client;


import lombok.RequiredArgsConstructor;
import org.example.webapp.dto.forum.issue.*;
import org.example.webapp.util.BadRequestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class IssueRestClientImpl implements IssueRestClient {
    private final RestClient restClient;
    private static final ParameterizedTypeReference<List<IssueDto>> ISSUE_LIST_TYPE = new ParameterizedTypeReference<>() {
    };

    @Override
    public List<IssueDto> getIssues(GetIssuesApiDto dto) {
        try {
            return restClient
                    .post()
                    .uri("")
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(ISSUE_LIST_TYPE);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(extractError(exception));
        }
    }

    @Override
    public IssueDto getById(GetIssueByIdApiDto dto) {
        try {
            return restClient
                    .post()
                    .uri("/getById")
                    .body(dto)
                    .contentType(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(IssueDto.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            throw new BadRequestException(extractError(exception));
        }
    }

    @Override
    public void create(CreateIssueApiDto dto) {
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
    public void delete(DeleteIssueApiDto dto) {
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
