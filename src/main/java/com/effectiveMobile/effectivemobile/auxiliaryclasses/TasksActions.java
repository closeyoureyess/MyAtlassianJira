package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Tasks;

import com.effectiveMobile.effectivemobile.exeptions.MainException;

/**
 * <pre>
 *     Интерфейс с методами, связанными с {@link Tasks}
 * </pre>
 */
public interface TasksActions {

    /**
     * Метод, сра
     *
     * @param objectInt
     * @param constantsInt
     * @return
     *//*
    boolean compareIntWithConstants(Integer objectInt, Integer constantsInt);*/

    /**
     * Метод, сравнивающий, совпадает ли переданный пользователь с авторизованным
     *
     * @param customUsersDto
     */
    boolean isPrivilegeTasks(CustomUsersDto customUsersDto);

    /**
     * Метод, заполняющий некоторые поля значениями по умолчанию
     *
     * @param tasksDto
     * @return {@link TasksDto} с заполненными полями, у которых есть значение по умолчанию
     */
    TasksDto fillTaskPriorityAndTaskStatusFields(TasksDto tasksDto) throws MainException;

}
