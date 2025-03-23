package org.example.webapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Role role = (Role) object;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
