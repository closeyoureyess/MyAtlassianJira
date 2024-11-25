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
@NoArgsConstructor
@AllArgsConstructor
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private CustomUsers users;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Tasks task;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return Objects.equals(id, notes.id) && Objects.equals(users, notes.users) && Objects.equals(comments, notes.comments) && Objects.equals(task, notes.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users, comments, task);
    }
}
