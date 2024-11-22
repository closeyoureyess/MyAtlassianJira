package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ActionsFabricImpl implements ActionsFabric{

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public TasksActions createTasksActions() {
        return applicationContext.getBean(TasksActions.class);
    }

    @Override
    public UserActions createUserActions() {
        return applicationContext.getBean(UserActions.class);
    }

    @Override
    public DefaultSettingsActions createDefaultSettingsActions() {
        return applicationContext.getBean(DefaultSettingsActions.class);
    }
}
