package com.effectiveMobile.effectivemobile.repository;

import com.effectiveMobile.effectivemobile.entities.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {
}
