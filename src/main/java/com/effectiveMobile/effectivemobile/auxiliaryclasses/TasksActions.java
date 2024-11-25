package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.exeptions.NotEnoughRulesForEntity;

/**
 * <pre>
 *     Интерфейс с методами, связанными с {@link Tasks}
 * </pre>
 */
public interface TasksActions {

    /**
     * Метод, заполняющий некоторые поля значениями по умолчанию
     *
     * @param tasksDto
     * @return {@link TasksDto} с заполненными полями, у которых есть значение по умолчанию
     */
    TasksDto fillTaskPriorityAndTaskStatusFields(TasksDto tasksDto) throws MainException;

    /**
     * Метод, проверяющий поля {@link TasksDto}, которые пытается отредактировать пользователь
     *
     * @param tasksDto DTO объекта задачи, который нужно проверить
     * @return true, если редактирование разрешено
     * @throws NotEnoughRulesForEntity Если редактирование запрещено из-за недостаточных прав
     */
    boolean fieldsTasksAllowedForEditing(TasksDto tasksDto) throws NotEnoughRulesForEntity;
}
