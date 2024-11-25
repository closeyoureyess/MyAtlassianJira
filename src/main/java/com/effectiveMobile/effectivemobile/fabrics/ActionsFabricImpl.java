package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.DefaultSettingsActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.TasksActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import lombok.RequiredArgsConstructor;
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