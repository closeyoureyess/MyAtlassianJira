package com.effectiveMobile.effectivemobile.entities;

import com.effectiveMobile.effectivemobile.other.DefaultSettingsFieldNameEnum;
import com.effectiveMobile.effectivemobile.other.TaskPriorityEnum;
import com.effectiveMobile.effectivemobile.other.TaskStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * <pre>
 *     Класс-entity с настройками по умолчанию, которые может настраивать администратор и которые являются общими для всех юзеров
 *     в системе
 * </pre>
 */
@Entity
@Table(name = "default_settings")
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
    @NotNull(message = "Необходимо указать наименование настройки, которую требуется отредактировать")
    private DefaultSettingsFieldNameEnum fieldName;

    @Column(name = "default_task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatusEnum defaultTaskStatus;

    @Column(name = "default_tasks_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriorityEnum defaultTaskPriority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultSettings that = (DefaultSettings) o;
        return Objects.equals(id, that.id) && fieldName == that.fieldName && defaultTaskStatus == that.defaultTaskStatus && defaultTaskPriority == that.defaultTaskPriority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fieldName, defaultTaskStatus, defaultTaskPriority);
    }
}
