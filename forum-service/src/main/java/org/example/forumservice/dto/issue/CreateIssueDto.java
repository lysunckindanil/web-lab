package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link org.example.forumservice.entity.Issue}
 */
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