package org.example.forumservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    String username;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    Boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Role> roles = new ArrayList<>();

}
