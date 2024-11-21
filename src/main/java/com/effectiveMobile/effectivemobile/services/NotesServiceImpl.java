package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotesServiceImpl implements NotesService{
    @Override
    public NotesDto createNotes(NotesDto notesDto) {
        log.info("Метод createNotes() " + notesDto.getId());
        return null;
    }
}
