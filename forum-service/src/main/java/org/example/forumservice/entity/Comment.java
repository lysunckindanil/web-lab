package org.example.forumservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String content;
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @ManyToOne
    private User author;
    @ManyToOne
    private Issue issue;
}
