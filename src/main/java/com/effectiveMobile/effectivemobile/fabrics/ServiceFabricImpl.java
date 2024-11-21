package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.services.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class ServiceFabricImpl implements ServiceFabric{
    @Override
    public JwtService createJwtService() {
        return new JwtServiceImpl();
    }

    @Override
    public TaskService createTaskService() {
        return new TaskServiceImpl();
    }

    @Override
    public UserService createUserService() {
        return new UserServiceImpl();
    }

    @Override
    public UserDetailsService createUserDetailsService() {
        return new MyUserDetailService();
    }

    @Override
    public NotesService createNotesService() {
        return new NotesServiceImpl();
    }

    @Override
    public DefaultSettingsService createDefaultSettingsService() {
        return new DefaultSettingsServiceImpl();
    }
}
