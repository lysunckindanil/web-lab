package org.example.forumservice.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
public class GetByIssueDto implements Serializable {
    @NotNull
    private final Long issueId;
}
