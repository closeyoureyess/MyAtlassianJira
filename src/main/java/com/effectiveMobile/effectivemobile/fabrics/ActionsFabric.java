package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.TasksActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;

/**
 * <pre>
 *     Фабрика для создания экземпляров классов-Actions
 * </pre>
 */
public interface ActionsFabric {

    /**
     * Фабричный метод для получения {@link TasksActions}
     * @return интерфейс TasksActions, которйы имплементируется TasksActionsImpl.class
     */
    TasksActions createTasksActions();

    /**
     * Фабричный метод для получения {@link UserActions}
     * @return интерфейс UserActions, который имплементируется UserActionsImpl.class
     */
    UserActions createUserActions();
}
