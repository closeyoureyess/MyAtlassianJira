package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.mapper.*;
import org.springframework.stereotype.Component;

@Component
public class MappersFabricImpl implements MappersFabric{

    @Override
    public NotesMapper createNotesMapper() {
        return new NotesMapperImpl();
    }

    @Override
    public TaskMapper createTaskMapper() {
        return new TaskMapperImpl();
    }

    @Override
    public UserMapper createUserMapper() {
        return new UserMapperImpl();
    }

    @Override
    public DefaultSettingsMapper createDefaultSettingsMapper() {
        return new DefaultSettingsMapperImpl();
    }
}
