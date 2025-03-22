package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link org.example.forumservice.model.Issue}
 */

@Builder
@AllArgsConstructor
@Getter
public class DeleteIssueDto implements Serializable {
    @NotNull
    private final Long issueId;
    @NotEmpty
    private final String username;
}