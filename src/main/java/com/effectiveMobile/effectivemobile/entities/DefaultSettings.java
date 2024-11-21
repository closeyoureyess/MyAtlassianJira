package com.effectiveMobile.effectivemobile.entities;

import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 *     Класс-entity с настройками по умолчанию, общими для всех
 * </pre>
 */
@Entity
@Table(name = "user_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "settings_field_name")
    @Enumerated(EnumType.STRING)
    private DefaultSettingsFieldNameEnum fieldName;

    @Column(name = "default_task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum defaultTaskStatus;

    @Column(name = "default_tasks_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriorityEnum defaultTaskPriority;
}
