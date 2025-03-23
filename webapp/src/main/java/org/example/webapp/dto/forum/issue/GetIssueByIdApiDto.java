package org.example.webapp.dto.forum.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetIssueByIdApiDto implements Serializable {
    private final Long issueId;
}