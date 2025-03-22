package org.example.webapp.dto.forum.issue;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;


@Builder
@AllArgsConstructor
public class CreateIssueApiDto implements Serializable {
    private final String title;
    private final String description;
    private final String author;
}