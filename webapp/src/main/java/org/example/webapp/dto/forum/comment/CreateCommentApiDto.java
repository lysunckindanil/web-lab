package org.example.webapp.dto.forum.comment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CreateCommentApiDto implements Serializable {
    private final String content;
    private final Long issueId;
    private final String authorUsername;
}