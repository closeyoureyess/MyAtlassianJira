package com.effectiveMobile.effectivemobile.repository;

import com.effectiveMobile.effectivemobile.entities.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    Optional<Tasks> findByHeader(String header);

    Page<Tasks> findAllByTaskAuthorId(Integer id, Pageable pageable);

    Page<Tasks> findAllByTaskExecutorId(Integer id, Pageable pageable);
}
