package org.example.webapp.dto.forum.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;


@Builder
@AllArgsConstructor
public class CreateCommentApiDto implements Serializable {
    private final String content;
    private final Long issueId;
    private final String authorUsername;
}