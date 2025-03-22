package org.example.webapp.dto.forum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@Getter
public class DeleteIssueApiDto implements Serializable {
    private final Long issueId;
    private final String username;
}