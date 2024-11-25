package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.mapper.DefaultSettingsMapper;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class MappersFabricImpl implements MappersFabric {

    private final NotesMapper notesMapper;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final DefaultSettingsMapper defaultSettingsMapper;

    @Override
    public NotesMapper createNotesMapper() {
        return notesMapper;
    }

    @Override
    public TaskMapper createTaskMapper() {
        return taskMapper;
    }

    @Override
    public UserMapper createUserMapper() {
        return userMapper;
    }

    @Override
    public DefaultSettingsMapper createDefaultSettingsMapper() {
        return defaultSettingsMapper;
    }
}
