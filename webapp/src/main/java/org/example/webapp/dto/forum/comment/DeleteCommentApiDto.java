package org.example.webapp.dto.forum.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;

@Builder
@AllArgsConstructor
public class DeleteCommentApiDto implements Serializable {
    private final Long commentId;
    private final String username;
}