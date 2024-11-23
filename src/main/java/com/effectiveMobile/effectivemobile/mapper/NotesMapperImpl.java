package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.entities.Notes;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class NotesMapperImpl implements NotesMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Notes convertDtoToNotes(NotesDto notesDto) {
        log.info("Метод convertDtoToNotes()");
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
        log.info("Метод convertNotesToDto()");
        if(userMapper == null) {
            userMapper = new UserMapperImpl();
        }
        TaskMapper taskMapper = new TaskMapperImpl();
        NotesDto localNotesDto = new NotesDto();
        if (notes != null) {
            localNotesDto.setId(notes.getId());
            localNotesDto.setComments(notes.getComments());
            localNotesDto.setUsersDto(userMapper.convertUserToDto(notes.getUsers()));
            localNotesDto.setTask(taskMapper.convertTasksToDto(notes.getTask()));
        }
        return localNotesDto;
    }

    @Override
    public List<NotesDto> transferListNotesToDto(List<Notes> notesList) {
        log.info("Метод transferListNotesToDto()");
        if (userMapper == null) {
            userMapper = new UserMapperImpl();
        }
        List<NotesDto> notesDtoList = new LinkedList<>();
        if (notesList != null) {
            for (int i = 0; i < notesList.size(); i++) {
                notesDtoList.add(new NotesDto(notesList.get(i).getId(), userMapper
                        .convertUserToDto(notesList.get(i).getUsers()),
                        notesList.get(i).getComments(), null));
            }
        }
        return notesDtoList;
    }

    @Override
    public List<Notes> transferDtoToListNotes(List<NotesDto> notesDtoList) {
        log.info("Метод transferListNotesToDto()");
        List<Notes> notesList = new LinkedList<>();
        if (notesDtoList != null) {
            for (int i = 0; i < notesDtoList.size(); i++) {
                notesList.add(new Notes(notesDtoList.get(i).getId(),
                        userMapper.convertDtoToUser(notesDtoList.get(i).getUsersDto()),
                        notesDtoList.get(i).getComments(), null));
            }
        }
        return notesList;
    }
}