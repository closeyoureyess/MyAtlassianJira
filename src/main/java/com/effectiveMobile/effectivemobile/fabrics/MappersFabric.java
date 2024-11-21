package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.mapper.DefaultSettingsMapper;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;

/**
 * <pre>
 *     Фабрика для создания экземпляров классов-Mappers
 * </pre>
 */
public interface MappersFabric {

    NotesMapper createNotesMapper();

    /**
     * Фабричный метод для получения {@link TaskMapper}
     *
     * @return интерфейс TaskMapper, который имплементируется TaskMapperImpl.class
     */
    TaskMapper createTaskMapper();

    /**
     * Фабричный метод для получения {@link UserMapper}
     *
     * @return интерфейс UserMapper, который имплементируется UserMapperImpl.class
     */
    UserMapper createUserMapper();

    /**
     * Фабричный метод для получения {@link DefaultSettingsMapper}
     *
     * @return интерфейс DefaultSettingsMapper, который имплементируется DefaultSettingsMapperImpl.class
     */
    DefaultSettingsMapper createDefaultSettingsMapper();
}
