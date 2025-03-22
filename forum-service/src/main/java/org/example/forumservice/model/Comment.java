package org.example.forumservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;

    @Column(nullable = false, name = "content")
    private String content;
    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(nullable = false, name = "author_id")
    private User author;
    @ManyToOne
    @JoinColumn(nullable = false, name = "issue_id")
    private Issue issue;
}
