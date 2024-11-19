package com.effectiveMobile.effectivemobile.entities;

import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * <pre>
 *     Класс-entity с задачами
 * </pre>
 */
@Entity
@Table(name = "tasks_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tasks_executor_id")
    private CustomUsers taskExecutor;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tasks_author_id")
    private CustomUsers taskAuthor;

    @Column(name = "header_tasks")
    private String header;

    @Column(name = "status_tasks")
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum taskStatus;

    @Column(name = "tasks_description")
    private String description;

    @Column(name = "tasks_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriorityEnum taskPriority;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tasks_notes_id")
    private Notes notes;

}
