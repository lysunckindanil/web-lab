package org.example.webapp.dto.forum.comment;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class CreateCommentDto implements Serializable {
    private String content;
    private final Long issueId;
}