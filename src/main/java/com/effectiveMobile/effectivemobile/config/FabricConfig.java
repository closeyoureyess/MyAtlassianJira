/*
package com.effectiveMobile.effectivemobile.config;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.TasksActions;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.mapper.TaskMapper;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import com.effectiveMobile.effectivemobile.services.JwtService;
import com.effectiveMobile.effectivemobile.services.TaskService;
import com.effectiveMobile.effectivemobile.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

*/
/**
 * <pre>
 *     Конфигурационный класс с бинами для интерфейсов-фабрик
 * </pre>
 *//*

@Configuration
public class FabricConfig {

    */
/**
     * Метод-бин, задающий поведение для методов MappersFabric
     *
     * @param notesMapper
     * @param taskMapper
     * @param userMapper
     * @return Возвращает MappersFabric с переопределенными методами
     *//*

    @Bean
    public MappersFabric mappersFabric(NotesMapper notesMapper, TaskMapper taskMapper, UserMapper userMapper) {
        return new MappersFabric() {
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
        };
    }

    */
/**
     * Метод-бин, задающий поведение для методов ActionsFabric
     *
     * @param userActions
     * @param tasksActions
     * @return Возвращает ActionsFabric с переопределенными методами
     *//*

    @Bean
    public ActionsFabric actionsFabric(UserActions userActions, TasksActions tasksActions) {
        return new ActionsFabric() {
            @Override
            public TasksActions createTasksActions() {
                return tasksActions;
            }

            @Override
            public UserActions createUserActions() {
                return userActions;
            }
        };
    }

    */
/**
     * @param taskService
     * @param userService
     * @param jwtService
     * @param userDetailsService
     * @return Возвращает ServiceFabric с переопределенными методами
     *//*

    @Bean
    public ServiceFabric serviceFabric(TaskService taskService, UserService userService, JwtService jwtService,
                                       UserDetailsService userDetailsService) {
        return new ServiceFabric() {

            @Override
            public JwtService createJwtService() {
                return jwtService;
            }

            @Override
            public TaskService createTaskService() {
                return taskService;
            }

            @Override
            public UserService createUserService() {
                return userService;
            }

            @Override
            public UserDetailsService createUserDetailsService() {
                return userDetailsService;
            }
        };
    }
}
*/
