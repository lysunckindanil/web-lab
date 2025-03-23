package org.example.forumservice.dto.issue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.forumservice.util.user.UserExists;

import java.io.Serializable;

/**
 * DTO for {@link org.example.forumservice.model.Issue}
 */

@Builder
@AllArgsConstructor
@Getter
public class CreateIssueDto implements Serializable {
    @NotBlank(message = "Название не должно быть пустым")
    private final String title;
    @NotBlank(message = "Описание не должно быть пустым")
    private final String description;
    @NotEmpty
    @UserExists
    private final String author;
}