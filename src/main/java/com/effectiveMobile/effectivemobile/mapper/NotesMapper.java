package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.entities.Notes;

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
     * @param notesDto - объект-комментарий
     * @return {@link Notes}
     */
    Notes convertDtoToNotes(NotesDto notesDto);

    /**
     * Метод для конвертации Notes в NotesDto
     *
     * @param notes - объект-комментарий
     * @return {@link NotesDto}
     */
    NotesDto convertNotesToDto(Notes notes);

    /**
     * Метод для конвертации списка с Notes в список с NotesDto
     *
     * @param tasksList - список с объектами-комментариями
     * @return {@link List<NotesDto>}
     */
    List<NotesDto> transferListNotesToDto(List<Notes> tasksList);

    /**
     * Метод для конвертации списка с NotesDto в список с Notes
     *
     * @param notesDtoList - список с объектами-комментариями
     * @return {@link List<Notes>}
     */
    List<Notes> transferDtoToListNotes(List<NotesDto> notesDtoList);

}
