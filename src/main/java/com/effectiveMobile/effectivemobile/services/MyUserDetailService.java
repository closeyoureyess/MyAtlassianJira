package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <pre>
 *     Сервис для загрузки деталей пользователя для аутентификации.
 * </pre>
 */
@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private AuthorizationRepository authorizationRepository;

    /**
     * Загружает данные пользователя по его email для аутентификации.
     *
     * @param email email пользователя
     * @return детали пользователя {@link UserDetails}
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Метод loadUserByUsername()" + email);
        Optional<CustomUsers> userFromDB = authorizationRepository.findByEmail(email);
        if(userFromDB.isPresent()) {
            CustomUsers newCustomUsers = userFromDB.get();
            return User.builder()
                    .username(newCustomUsers.getEmail())
                    .password(newCustomUsers.getPasswordKey())
                    .roles(getRoles(newCustomUsers))
                    .build();
        } else {
            throw new UsernameNotFoundException(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription() + " " + userFromDB.get().getEmail());
        }

    }

    /**
     * Получает роль пользователя. Если роль не указана, возвращает роль по умолчанию.
     *
     * @param customUsers объект {@link CustomUsers}
     * @return роль пользователя
     */
    private String getRoles(CustomUsers customUsers) {
        log.info("Метод getRoles()" + customUsers.getId());
        if (customUsers.getRole() == null) {
            return ConstantsClass.USERROLE;
        } else {
            return customUsers.getRole();
        }
    }
}
