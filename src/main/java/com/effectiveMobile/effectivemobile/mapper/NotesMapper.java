package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.Notes;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.MainException;

import java.util.List;

/**
 * <pre>
 *     Маппер для {@link Notes}, {@link NotesDto}
 * </pre>
 */
public interface NotesMapper {

    /**
     * Метод для конвертации NotesDto в Notes
     *
     * @param notesDto
     * @return {@link Notes}
     */
    Notes convertDtoToNotes(NotesDto notesDto);

    /**
     * Метод для конвертации Notes в NotesDto
     *
     * @param notes
     * @return {@link NotesDto}
     */
    NotesDto convertNotesToDto(Notes notes);

    /**
     * Метод для конвертации списка с Notes в список с NotesDto
     *
     * @param tasksList
     * @return {@link List<NotesDto>}
     */
    List<NotesDto> transferListNotesToDto(List<Notes> tasksList);

    /**
     * Метод для конвертации списка с NotesDto в список с Notes
     *
     * @param notesDtoList
     * @return {@link List<Notes>}
     */
    List<Notes> transferDtoToListNotes(List<NotesDto> notesDtoList);

}
