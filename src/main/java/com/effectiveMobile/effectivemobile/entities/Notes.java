package com.effectiveMobile.effectivemobile.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private CustomUsers users;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Tasks task;

}
