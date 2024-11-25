package com.effectiveMobile.effectivemobile.entities;

import com.effectiveMobile.effectivemobile.other.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * <pre>
 *     Класс-entity с пользователями
 * </pre>
 */
@Entity
@Table(name = "users_credentials")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email_user")
    private String email;

    @Column(name = "password_user")
    private String passwordKey;

    @Column(name = "role_user")
    @Enumerated(value = EnumType.STRING)
    private UserRoles role;

    @OneToMany(mappedBy = "taskAuthor", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    private List<Tasks> authoredTasks;

    @OneToMany(mappedBy = "taskExecutor", fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH
    })
    private List<Tasks> executedTasks;

}
