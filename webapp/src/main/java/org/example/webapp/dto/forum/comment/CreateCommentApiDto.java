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
public class CreateCommentApiDto implements Serializable {
    private final String content;
    private final Long issueId;
    private final String authorUsername;
}