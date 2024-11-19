package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class TasksActionsImpl implements TasksActions{

    @Autowired
    private AuthorizationRepository authorizationRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean compareIntWithConstants(Integer objectInt, Integer constantsInt) {
        if (constantsInt.equals(ConstantsClass.REGIME_RECORD) && objectInt.equals(constantsInt)) {
            return true;
        } else if (constantsInt.equals(ConstantsClass.REGIME_OVERWRITING) && objectInt.equals(constantsInt)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isPrivilegeTasks(CustomUsersDto customUsersDto) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Optional<CustomUsers> customUsers = authorizationRepository.findByEmail(loggedInUser.getName());
        String emailCurrentUser = customUsers.get().getEmail();
        if (customUsersDto.getEmail().equals(emailCurrentUser)) {
            return true;
        }
        return false;
    }
}
