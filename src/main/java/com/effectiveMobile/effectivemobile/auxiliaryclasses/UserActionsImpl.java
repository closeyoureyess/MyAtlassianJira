package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserActionsImpl implements UserActions {

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private TasksActions tasksActions;

    @Override
    public Tasks checkFindUser(CustomUsers customUsers, Tasks newTasks, Integer typeOperations) throws UsernameNotFoundException {
        log.info("Метод checkFindUser() " + typeOperations);
        Optional<CustomUsers> optionalCustomUsers = searchUserEmailOrId(customUsers);

        if (optionalCustomUsers.isPresent() && (typeOperations.equals(ConstantsClass.ONE_FLAG))) {
            newTasks.setTaskExecutor(optionalCustomUsers.get());
            return newTasks;
        } else if (optionalCustomUsers.isPresent() && typeOperations.equals(ConstantsClass.ZERO_FLAG)) {
            newTasks.setTaskAuthor(optionalCustomUsers.get());
            return newTasks;
        } else {
            throw new UsernameNotFoundException(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription());
        }
    }

    @Override
    public Optional<CustomUsers> getCurrentUser() {
        log.info("Метод getCurrentUser()");
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return authorizationRepository.findByEmail(loggedInUser.getName());
    }

    @Override
    public Optional<String> getRoleCurrentAuthorizedUser(String roleToMatch) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Метод getRoleCurrentAuthorizedUser()" );
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> { log.info(authority); return authority.equals(roleToMatch); })
                .findFirst();
    }

    @Override
    public String getEmailCurrentUser(){
        log.info("Метод getEmailCurrentUser()");
        return getCurrentUser().get().getEmail();
    }

    @Override
    public Optional<CustomUsers> searchUserEmailOrId(CustomUsers customUsers) throws UsernameNotFoundException {
        log.info("Метод searchUserEmailOrId()");
        Optional<CustomUsers> optionalCustomUsers = Optional.empty();
        if (customUsers != null) {
            if (customUsers.getEmail() != null) {
                optionalCustomUsers = authorizationRepository.findByEmail(customUsers.getEmail());
            } else if (customUsers.getId() != null) {
                optionalCustomUsers = authorizationRepository.findById(customUsers.getId());
            }
        }
        return optionalCustomUsers;
    }
}
