package org.example.webapp.dto.forum;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateIssueDto implements Serializable {
    @NotEmpty(message = "Название не должно быть пустым.")
    private String title;
    @NotEmpty(message = "Описание не должно быть пустым.")
    private String description;
}