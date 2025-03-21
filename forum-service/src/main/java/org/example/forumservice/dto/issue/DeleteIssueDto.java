package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link org.example.forumservice.entity.Issue}
 */
@AllArgsConstructor
@Getter
public class DeleteIssueDto implements Serializable {
    @NotNull
    private final Long id;
    @NotEmpty
    private final String author;
}