package com.effectiveMobile.effectivemobile.services;

import com.effectiveMobile.effectivemobile.auxiliaryclasses.UserActions;
import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.dto.TasksDto;
import com.effectiveMobile.effectivemobile.entities.CustomUsers;
import com.effectiveMobile.effectivemobile.entities.Notes;
import com.effectiveMobile.effectivemobile.entities.Tasks;
import com.effectiveMobile.effectivemobile.exeptions.EntityNotBeNull;
import com.effectiveMobile.effectivemobile.exeptions.EntityNotFoundExeption;
import com.effectiveMobile.effectivemobile.exeptions.FieldNotBeNull;
import com.effectiveMobile.effectivemobile.fabrics.ActionsFabric;
import com.effectiveMobile.effectivemobile.fabrics.MappersFabric;
import com.effectiveMobile.effectivemobile.mapper.NotesMapper;
import com.effectiveMobile.effectivemobile.repository.NotesRepository;
import com.effectiveMobile.effectivemobile.repository.TasksRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class NotesServiceImplTest {

    private NotesServiceImpl notesService;
    private MappersFabric mockMappersFabric;
    private NotesRepository mockNotesRepository;
    private ActionsFabric mockActionsFabric;
    private TasksRepository mockTasksRepository;
    private NotesMapper mockNotesMapper;
    private UserActions userActions;

    @BeforeEach
    void setUp() {
        mockMappersFabric = Mockito.mock(MappersFabric.class);
        mockNotesRepository = Mockito.mock(NotesRepository.class);
        mockActionsFabric = Mockito.mock(ActionsFabric.class);
        mockTasksRepository = Mockito.mock(TasksRepository.class);
        mockNotesMapper = Mockito.mock(NotesMapper.class);
        userActions = Mockito.mock(UserActions.class);

        notesService = new NotesServiceImpl();
        notesService.setMappersFabric(mockMappersFabric);
        notesService.setNotesRepository(mockNotesRepository);
        notesService.setActionsFabric(mockActionsFabric);
        notesService.setTasksRepository(mockTasksRepository);

        Mockito.when(mockMappersFabric.createNotesMapper()).thenReturn(mockNotesMapper);
    }

    @Test
    @DisplayName("Тест: Успешное создание заметки")
    void testCreateNotesSuccess() throws EntityNotFoundExeption, EntityNotBeNull, FieldNotBeNull {
        NotesDto notesDto = new NotesDto();
        notesDto.setTask(new TasksDto());
        notesDto.getTask().setId(1);

        Tasks task = new Tasks();
        task.setId(1);

        CustomUsers user = new CustomUsers();
        user.setId(1);

        Notes notes = new Notes();
        notes.setId(1);
        notes.setUsers(user);
        notes.setTask(task);

        Mockito.when(mockTasksRepository.findById(1)).thenReturn(Optional.of(task));
        Mockito.when(mockActionsFabric.createUserActions()).thenReturn(userActions);
        Mockito.when(userActions.getCurrentUser()).thenReturn(Optional.of(user));
        Mockito.when(mockNotesMapper.convertDtoToNotes(notesDto)).thenReturn(notes);
        Mockito.when(mockNotesRepository.save(notes)).thenReturn(notes);
        Mockito.when(mockNotesMapper.convertNotesToDto(notes)).thenReturn(notesDto);

        NotesDto result = notesService.createNotes(notesDto);

        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(notesDto, result, "Результат должен совпадать с DTO");
        Mockito.verify(mockTasksRepository, Mockito.times(1)).findById(1);
        Mockito.verify(mockActionsFabric.createUserActions(), Mockito.times(1)).getCurrentUser();
        Mockito.verify(mockNotesMapper, Mockito.times(1)).convertDtoToNotes(notesDto);
        Mockito.verify(mockNotesRepository, Mockito.times(1)).save(notes);
        Mockito.verify(mockNotesMapper, Mockito.times(1)).convertNotesToDto(notes);
    }

    @Test
    @DisplayName("Тест: Выброс исключения, если объект задачи null")
    void testCreateNotesTaskEntityNull() {
        NotesDto notesDto = new NotesDto();

        EntityNotBeNull exception = Assertions.assertThrows(EntityNotBeNull.class, () -> {
            notesService.createNotes(notesDto);
        });

        Assertions.assertEquals("Передаваемый объект задача не может быть null", exception.getMessage(), "Сообщение об ошибке должно совпадать");
    }

    @Test
    @DisplayName("Тест: Выброс исключения, если идентификатор задачи null")
    void testCreateNotesTaskIdNull() {
        NotesDto notesDto = new NotesDto();
        notesDto.setTask(new TasksDto());

        FieldNotBeNull exception = Assertions.assertThrows(FieldNotBeNull.class, () -> {
            notesService.createNotes(notesDto);
        });

        Assertions.assertEquals("Возникла ошибка в системе: Передаваемый идентификатор задачи не может быть null", exception.getMessage(), "Сообщение об ошибке должно совпадать");
    }

    @Test
    @DisplayName("Тест: Выброс исключения, если задача не найдена")
    void testCreateNotesTaskNotFound() {
        NotesDto notesDto = new NotesDto();
        notesDto.setTask(new TasksDto());
        notesDto.getTask().setId(1);

        Mockito.when(mockTasksRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundExeption exception = Assertions.assertThrows(EntityNotFoundExeption.class, () -> {
            notesService.createNotes(notesDto);
        });

        Assertions.assertEquals("Существующей задачи по переданному id не найдено. Проверьте корректность указанного идентификатора и попробуйте еще раз.", exception.getMessage(), "Сообщение об ошибке должно совпадать");
    }
}