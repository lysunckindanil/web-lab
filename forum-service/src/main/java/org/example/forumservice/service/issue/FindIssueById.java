package org.example.forumservice.service.issue;

import org.example.forumservice.model.Issue;

public interface FindIssueById {
    Issue find(Long issueId);
}
