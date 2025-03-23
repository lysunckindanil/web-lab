package org.example.webapp.dto.forum.issue;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CreateIssueApiDto implements Serializable {
    private final String title;
    private final String description;
    private final String author;
}