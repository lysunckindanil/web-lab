package org.example.webapp.dto.forum.comment;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetCommentsByIssueDto implements Serializable {
    private final Long issueId;
}
