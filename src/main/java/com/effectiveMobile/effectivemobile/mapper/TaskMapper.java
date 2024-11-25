package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.ExecutorNotFoundExeption;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * <pre>
 *     Маппер для {@link Tasks}, {@link TasksDto}
 * </pre>
 */
public interface TaskMapper {

    /**
     * Метод, конвертирующий {@link TasksDto} в {@link Tasks}
     *
     * @param tasksDto DTO объекта задачи
     * @param method Дополнительные параметры для определения способа преобразования
     * @return Сконвертированный объект задачи
     * @throws ExecutorNotFoundExeption Если исполнитель задачи не найден
     */
    Tasks convertDtoToTasks(TasksDto tasksDto, Integer... method) throws ExecutorNotFoundExeption;

    /**
     * Метод, конвертирующий {@link Tasks} в {@link TasksDto}
     *
     * @param tasks Сущность задачи из базы данных
     * @return Сконвертированный DTO объекта задачи
     */
    TasksDto convertTasksToDto(Tasks tasks);

    /**
     * Метод, конвертирующий List<Tasks> в List c Dto
     *
     * @param tasksList Список сущностей задач из базы данных
     * @return Список сконвертированных DTO объектов задач
     */
    List<TasksDto> transferListTasksToDto(List<Tasks> tasksList);

    /**
     * Метод, сравнивающий поля Tasks из БД с TasksDto
     *
     * @param tasksDto DTO объекта задачи
     * @param tasks Сущность задачи из базы данных
     * @return Обновленный объект задачи {@link Tasks}
     * @throws UsernameNotFoundException Если пользователь, связанный с задачей, не найден
     */
    Tasks compareTaskAndDto(TasksDto tasksDto, Tasks tasks) throws UsernameNotFoundException;

}
