package com.effectiveMobile.effectivemobile.fabrics;

import com.effectiveMobile.effectivemobile.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceFabricImpl implements ServiceFabric {

    private final JwtService jwtService;
    private final TaskService taskService;
    private final MyUserDetailService myUserDetailService; // Предполагается, что это класс, реализующий UserDetailsService
    private final NotesService notesService;
    private final DefaultSettingsService defaultSettingsService;

    @Override
    public JwtService createJwtService() {
        return jwtService;
    }

    @Override
    public TaskService createTaskService() {
        return taskService;
    }

    @Override
    public UserDetailsService createUserDetailsService() {
        return myUserDetailService;
    }

    @Override
    public NotesService createNotesService() {
        return notesService;
    }

    @Override
    public DefaultSettingsService createDefaultSettingsService() {
        return defaultSettingsService;
    }
}
/*@Component
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
}*/
