package com.effectiveMobile.effectivemobile.services;


import com.effectiveMobile.effectivemobile.dto.CustomUsersDto;
import com.effectiveMobile.effectivemobile.dto.RegistrationUsers;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.fabrics.ServiceFabric;
import com.effectiveMobile.effectivemobile.mapper.UserMapper;
import com.effectiveMobile.effectivemobile.other.UserRoles;
import com.effectiveMobile.effectivemobile.repository.AuthorizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private MappersFabric mappersFabric;

    @Autowired
    @Qualifier("myUserDetailService")
    private UserDetailsService userDetailsService;

    @Autowired
    private ServiceFabric serviceFabric;

    @Autowired
    private ActionsFabric actionsFabric;

    @Autowired
    private AuthorizationRepository authorizationRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public CustomUsersDto createUser(CustomUsersDto customUsersDto) {
        log.info("Метод createUser() ");
        UserMapper userMapper = mappersFabric.createUserMapper();
        Optional<CustomUsers> optionalCustomUsers = actionsFabric
                .createUserActions()
                .searchUserEmailOrId(userMapper.convertDtoToUser(customUsersDto));
        if (optionalCustomUsers.isPresent()) {
            log.info("User already exists!");
            return userMapper.convertUserToDto(optionalCustomUsers.get());
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (UserRoles.ADMIN != customUsersDto.getRole() && UserRoles.USER != customUsersDto.getRole()) {
            customUsersDto.setRole(UserRoles.USER);
        }
        CustomUsers customUsers = userMapper.convertDtoToUser(customUsersDto);
        customUsers.setId(null);
        customUsers.setPasswordKey(passwordEncoder.encode(customUsers.getPasswordKey()));
        return userMapper.convertUserToDto(
                authorizationRepository.save(customUsers)
        );
    }

    @Override
    public String authorizationUser(RegistrationUsers registrationUsers) throws UsernameNotFoundException {
        log.info("Метод authorizationUser() " + registrationUsers.getEmail());
        String userEmail = registrationUsers.getEmail();
        String passwordKey = registrationUsers.getPasswordKey();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userEmail, passwordKey
                ));
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            return serviceFabric.createJwtService().generateToken(userDetails);
        } else {
            throw new UsernameNotFoundException(DescriptionUserExeption.USER_NOT_FOUND.getEnumDescription() + " " + userEmail);
        }
    }
}
