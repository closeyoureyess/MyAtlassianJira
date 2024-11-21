package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Notes;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.EntityNotBeNull;
import com.effectiveMobile.effectivemobile.exeptions.EntityNotFoundExeption;
import com.effectiveMobile.effectivemobile.exeptions.FieldNotBeNull;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.repository.NotesRepository;
import com.effectiveMobile.effectivemobile.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.effectiveMobile.effectivemobile.exeptions.DescriptionUserExeption.*;

@Service
@Slf4j
public class NotesServiceImpl implements NotesService{

    @Autowired
    private MappersFabric mappersFabric;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private UserActions userActions;

    @Autowired
    private TasksRepository tasksRepository;

    @Override
    @Transactional
    public NotesDto createNotes(NotesDto notesDto) throws EntityNotFoundExeption, EntityNotBeNull, FieldNotBeNull {
        log.info("Метод createNotes() " + notesDto.getId());
        NotesMapper notesMapper = mappersFabric.createNotesMapper();
        Optional<Tasks> optionalTasks;
        if (notesDto.getTask() != null) {
            if (notesDto.getTask().getId() != null) {
                Integer taskId = notesDto.getTask().getId();
                optionalTasks = tasksRepository.findById(taskId);
            } else {
                throw new FieldNotBeNull(GENERATION_ERROR.getEnumDescription(),
                        new FieldNotBeNull(ID_TASKS_NOT_BE_NULL.getEnumDescription()));
            }
        } else {
            throw new EntityNotBeNull(GENERATION_ERROR.getEnumDescription(),
                    new EntityNotBeNull(TASKS_ENTITY_NOT_BE_NULL.getEnumDescription()));
        }
        if (optionalTasks.isEmpty()) {
            throw new EntityNotFoundExeption(GENERATION_ERROR.getEnumDescription(),
                    new EntityNotFoundExeption(TASKS_ENTITY_NOT_FOUND.getEnumDescription()));
        }
        Tasks tasks = optionalTasks.get();

        Optional<CustomUsers> optionalAuthorizedUser = userActions.getCurrentUser();
        CustomUsers authorizedUser = optionalAuthorizedUser.get();
        Notes notes = notesMapper.convertDtoToNotes(notesDto);
        notes.setUsers(authorizedUser);
        notes.setTask(tasks);
        notes = notesRepository.save(notes);
        return notesMapper.convertNotesToDto(notes);
    }
}
