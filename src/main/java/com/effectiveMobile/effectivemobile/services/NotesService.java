package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.exeptions.MainException;

/**
 * <pre>
 *     Интерфейс для работы с сущностью Notes
 * </pre>
 */
public interface NotesService {

    NotesDto createNotes (NotesDto notesDto) throws MainException;

}
