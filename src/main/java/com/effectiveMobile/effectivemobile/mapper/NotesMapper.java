package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.entities.Notes;

public interface NotesMapper {

    Notes convertDtoToNotes(NotesDto notesDto);
    NotesDto convertNotesToDto(Notes notes);

}
