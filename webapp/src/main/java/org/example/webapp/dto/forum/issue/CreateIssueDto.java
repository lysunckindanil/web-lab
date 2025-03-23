package org.example.webapp.dto.forum.issue;

import lombok.Data;

import java.io.Serializable;


@Data
public class CreateIssueDto implements Serializable {
    private String title;
    private String description;
}