package org.example.webapp.dto.forum.comment;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
public class DeleteCommentApiDto implements Serializable {
    private final Long commentId;
    private final String username;
}