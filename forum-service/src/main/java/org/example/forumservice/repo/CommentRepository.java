package org.example.forumservice.repo;

import org.example.forumservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select comment from Comment comment where comment.issue.id = :issue order by comment.createdAt desc")
    List<Comment> getByIssueId(@Param("issue") Long issueId);
}