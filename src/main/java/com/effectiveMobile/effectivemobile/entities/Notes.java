package com.effectiveMobile.effectivemobile.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * <pre>
 *     Класс-entity для комментариев к задаче
 * </pre>
 */
@Entity
@Table(name = "notes_comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUsers users;


    @Column(name = "comments")
    private String comments;


    public Notes(CustomUsers users, String comments) {
        this.users = users;
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return id == notes.id && Objects.equals(users, notes.users) && Objects.equals(comments, notes.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, comments);
    }
}
