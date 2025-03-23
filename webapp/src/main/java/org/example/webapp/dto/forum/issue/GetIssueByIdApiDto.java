package org.example.webapp.dto.forum.issue;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class GetIssueByIdApiDto implements Serializable {
    private final Long issueId;
}