package org.example.webapp.dto.forum.comment;

import lombok.Data;

import java.io.Serializable;


@Data
public class CreateCommentDto implements Serializable {
    private String content;
    private final Long issueId;
}