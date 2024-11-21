package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.*;
import org.springframework.stereotype.Component;

@Component
public class ActionsFabricImpl implements ActionsFabric{
    @Override
    public TasksActions createTasksActions() {
        return new TasksActionsImpl();
    }

    @Override
    public UserActions createUserActions() {
        return new UserActionsImpl();
    }

    @Override
    public DefaultSettingsActions createDefaultSettingsActions() {
        return new DefaultSettingsActionsImpl();
    }
}
