package org.example.webapp.dto.forum.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;


@Data
public class CreateCommentDto implements Serializable {
    @NotBlank(message = "Содержание комментария не должно быть пустым")
    private String content;
    private final Long issueId;
}