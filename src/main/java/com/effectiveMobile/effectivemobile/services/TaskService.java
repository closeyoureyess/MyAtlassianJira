package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.exeptions.MainException;
import com.effectiveMobile.effectivemobile.dto.TasksDto;

import java.util.List;
import java.util.Optional;

/**
 * <pre>
 *     Интерфейс для работы с сущностью Tasks
 * </pre>
 */
public interface TaskService {

    /**
     * Метод для создания задачи
     * @param tasksDto
     * @return Возвращает созданный объект-задачу
     * @throws MainException
     */
    TasksDto createTasks(TasksDto tasksDto) throws MainException;

    /**
     * Метод для редактирования задачи
     * @param tasksDto
     * @return Optional с измененным объектом-задачей
     * @throws MainException
     */
    Optional<TasksDto> changeTasks(TasksDto tasksDto) throws MainException;

    /**
     * Метод для получения задач по автору или исполнителю
     * @param authorOrExecutor
     * @param offset
     * @param limit
     * @param flag
     * @return Список с задачами по автору или исполнителю
     */
    Optional<List<TasksDto>> getTasksOfAuthorOrExecutor(String authorOrExecutor, Integer offset, Integer limit, Integer flag);

    /**
     * Метод для удаления задачи
     * @param idTasks
     * @return Результат удаления задачи в формате boolean
     */
    boolean deleteTasks(Integer idTasks);

}
