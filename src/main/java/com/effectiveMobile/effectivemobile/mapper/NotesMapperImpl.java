package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.entities.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotesMapperImpl implements NotesMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Notes convertDtoToNotes(NotesDto notesDto) {
        Notes localNotes = new Notes();
        if (notesDto != null) {
            localNotes.setId(notesDto.getId());
            localNotes.setComments(notesDto.getComments());
            localNotes.setUsers(userMapper.convertDtoToUser(notesDto.getUsersDto()));
        }
        return localNotes;
    }

    @Override
    public NotesDto convertNotesToDto(Notes notes) {
        NotesDto localNotesDto = new NotesDto();
        if (notes != null) {
            localNotesDto.setId(notes.getId());
            localNotesDto.setComments(notes.getComments());
            localNotesDto.setUsersDto(userMapper.convertUserToDto(notes.getUsers()));
        }
        return localNotesDto;
    }
}