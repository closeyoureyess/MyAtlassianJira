package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionsFabricImpl implements ActionsFabric {

    private final TasksActions tasksActions;
    private final UserActions userActions;
    private final DefaultSettingsActions defaultSettingsActions;

    @Override
    public TasksActions createTasksActions() {
        return tasksActions;
    }

    @Override
    public UserActions createUserActions() {
        return userActions;
    }

    @Override
    public DefaultSettingsActions createDefaultSettingsActions() {
        return defaultSettingsActions;
    }
}
/*@Component
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
}*/
