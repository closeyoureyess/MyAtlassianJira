package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class ServiceFabricImpl implements ServiceFabric{

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public JwtService createJwtService() {
        return applicationContext.getBean(JwtService.class);
    }

    @Override
    public TaskService createTaskService() {
        return applicationContext.getBean(TaskService.class);
    }

    @Override
    public UserService createUserService() {
        return applicationContext.getBean(UserService.class);
    }

    @Override
    public UserDetailsService createUserDetailsService() {
        return applicationContext.getBean(MyUserDetailService.class);
    }

    @Override
    public NotesService createNotesService() {
        return applicationContext.getBean(NotesService.class);
    }

    @Override
    public DefaultSettingsService createDefaultSettingsService() {
        return applicationContext.getBean(DefaultSettingsService.class);
    }
}
