package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.exeptions.ExecutorNotFoundExeption;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * <pre>
 *     Маппер для {@link Tasks}
 * </pre>
 */
public interface TaskMapper {

    /**
     * Метод, конвертирующий {@link TasksDto} в {@link Tasks}
     * @param tasksDto
     * @param method
     * @return сконвертированный {@link Tasks}
     * @throws ExecutorNotFoundExeption
     */
    Tasks convertDtoToTasks(TasksDto tasksDto, Integer... method) throws ExecutorNotFoundExeption;

    /**
     * Метод, конвертирующий {@link Tasks} в {@link TasksDto}
     * @param tasks
     * @return сконвертированный {@link TasksDto}
     */
    TasksDto convertTasksToDto(Tasks tasks);

    /**
     * Метод, конвертирующий List<Tasks> в List c Dto
     * @param tasksList
     * @return сконвертированный List<TasksDto>
     */
    List<TasksDto> transferListTasksToDto(List<Tasks> tasksList);

    /**
     * Метод, сравнивающий поля Tasks из БД с TasksDto
     * @param tasksDto
     * @param tasks
     * @return отредактированный {@link Tasks}
     * @throws UsernameNotFoundException
     */
    Tasks compareTaskAndDto(TasksDto tasksDto, Tasks tasks) throws UsernameNotFoundException;

}
