package org.example.webapp.dto.forum.comment;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteCommentDto implements Serializable {
    private final Long commentId;
}