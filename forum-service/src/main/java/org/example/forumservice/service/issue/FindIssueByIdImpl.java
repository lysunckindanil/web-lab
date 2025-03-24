package org.example.forumservice.service.issue;

import lombok.RequiredArgsConstructor;
import org.example.forumservice.model.Issue;
import org.example.forumservice.repo.IssueRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindIssueByIdImpl implements FindIssueById {
    private final IssueRepository issueRepository;

    @Override
    public Issue find(Long issueId) {
        return issueRepository.findById(issueId).get();
    }
}
