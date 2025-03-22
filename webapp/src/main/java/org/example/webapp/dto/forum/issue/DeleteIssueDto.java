package org.example.webapp.dto.forum.issue;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@Getter
public class DeleteIssueDto implements Serializable {
    @NotNull
    private final Long issueId;
}