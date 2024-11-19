package com.effectiveMobile.effectivemobile.services;


import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.auxiliaryclasses.LoginForm;
import com.effectiveMobile.effectivemobile.constants.ConstantsClass;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private ActionsFabric actionsFabric;

    @Override
    @Transactional
    public CustomUsersDto createUser(CustomUsersDto customUsersDto) {
        Optional<CustomUsers> optionalCustomUsers = actionsFabric
                .createUserActions()
                .searchUserEmailOrId(userMapper.convertDtoToUser(customUsersDto));
        if (optionalCustomUsers.isPresent()) {
            log.info("User already exists!");
            return userMapper.convertUserToDto(optionalCustomUsers.get());
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!ConstantsClass.ADMINROLE.equals(customUsersDto.getRole()) && !ConstantsClass.USERROLE.equals(customUsersDto.getRole())) {
            customUsersDto.setRole(ConstantsClass.USERROLE);
        }
        CustomUsers customUsers = userMapper.convertDtoToUser(customUsersDto);
        customUsers.setPasswordKey(passwordEncoder.encode(customUsers.getPasswordKey()));
        return userMapper.convertUserToDto(
                authorizationRepository.save(customUsers)
        );
    }

    @Override
    @Transactional
    public String authorizationUser(LoginForm loginForm) throws UsernameNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(), loginForm.getPasswordKey()
                ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(myUserDetailService.loadUserByUsername(loginForm.getEmail()));
        } else {
            throw new UsernameNotFoundException(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription() + " " + loginForm.getEmail());
        }
    }

}
