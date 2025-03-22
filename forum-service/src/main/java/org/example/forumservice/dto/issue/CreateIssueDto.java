package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotEmpty;
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
public class CreateIssueDto implements Serializable {
    @NotEmpty
    private final String title;
    @NotEmpty
    private final String description;
    @NotEmpty
    private final String author;
}