package com.effectiveMobile.effectivemobile.repository;

import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<CustomUsers, Integer> {
    Optional<CustomUsers> findByEmail(String email);
}
