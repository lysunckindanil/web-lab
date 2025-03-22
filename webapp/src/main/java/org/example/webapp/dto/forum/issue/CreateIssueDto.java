package org.example.webapp.dto.forum.issue;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;


@Data
public class CreateIssueDto implements Serializable {
    @NotBlank(message = "Название не должно быть пустым")
    private String title;
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
}