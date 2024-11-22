package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MappersFabricImpl implements MappersFabric{

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public NotesMapper createNotesMapper() {
        return applicationContext.getBean(NotesMapper.class);
    }

    @Override
    public TaskMapper createTaskMapper() {
        return applicationContext.getBean(TaskMapper.class);
    }

    @Override
    public UserMapper createUserMapper() {
        return applicationContext.getBean(UserMapper.class);
    }

    @Override
    public DefaultSettingsMapper createDefaultSettingsMapper() {
        return applicationContext.getBean(DefaultSettingsMapper.class);
    }
}
