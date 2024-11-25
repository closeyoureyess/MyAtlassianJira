package com.effectiveMobile.effectivemobile.entities;

import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tasks_executor_id")
    private CustomUsers taskExecutor;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notes> notes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tasks tasks = (Tasks) o;
        return Objects.equals(id, tasks.id) && Objects.equals(taskExecutor, tasks.taskExecutor) && Objects.equals(taskAuthor, tasks.taskAuthor) && Objects.equals(header, tasks.header) && taskStatus == tasks.taskStatus && Objects.equals(description, tasks.description) && taskPriority == tasks.taskPriority && Objects.equals(notes, tasks.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskExecutor, taskAuthor, header, taskStatus, description, taskPriority, notes);
    }
}
