package org.example.webapp.dto.forum.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.io.Serializable;

@ToString
@Builder
@AllArgsConstructor
public class DeleteCommentApiDto implements Serializable {
    private final Long commentId;
    private final String username;
}