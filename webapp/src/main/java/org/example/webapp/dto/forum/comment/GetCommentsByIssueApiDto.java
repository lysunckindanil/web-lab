package org.example.webapp.dto.forum.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetCommentsByIssueApiDto implements Serializable {
    private final Long issueId;
}
