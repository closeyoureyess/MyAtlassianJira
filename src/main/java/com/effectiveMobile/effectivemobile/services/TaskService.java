package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;

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
     *
     * @param tasksDto DTO объекта задачи, содержащий данные для создания
     * @return Созданный объект-задача в виде {@link TasksDto}
     * @throws MainException Если возникла ошибка при создании задачи
     */
    TasksDto createTasks(TasksDto tasksDto) throws MainException;

    /**
     * Метод для редактирования задачи
     *
     * @param tasksDto DTO объекта задачи с обновленными данными
     * @return {@link Optional} с изменённым объектом-задачей, если задача найдена и успешно обновлена
     * @throws MainException Если возникла ошибка при редактировании задачи
     */
    Optional<TasksDto> changeTasks(TasksDto tasksDto) throws MainException;

    /**
     * Метод для получения задач по автору или исполнителю
     *
     * @param authorOrExecutor Email автора или исполнителя задачи
     * @param offset Номер страницы для пагинации
     * @param limit Лимит записей на странице
     * @param flag Флаг, определяющий поиск (1 - задачи автора, 0 - задачи исполнителя)
     * @return {@link Optional} со списком задач в виде {@link List<TasksDto>}
     */
    Optional<List<TasksDto>> getTasksOfAuthorOrExecutor(String authorOrExecutor, Integer offset, Integer limit, Integer flag);

    /**
     * Метод для удаления задачи
     *
     * @param idTasks ID задачи, которую нужно удалить
     * @return true, если задача была успешно удалена, иначе false
     */
    boolean deleteTasks(Integer idTasks);

}
