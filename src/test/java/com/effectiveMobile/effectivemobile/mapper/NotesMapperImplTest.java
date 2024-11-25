package com.effectiveMobile.effectivemobile.mapper;

import com.effectiveMobile.effectivemobile.dto.NotesDto;
import com.effectiveMobile.effectivemobile.entities.Notes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class NotesMapperImplTest {

    private NotesMapperImpl notesMapper;
    private UserMapper mockUserMapper;

    @BeforeEach
    void setUp() {
        mockUserMapper = Mockito.mock(UserMapper.class);
        notesMapper = new NotesMapperImpl();
        notesMapper.setUserMapper(mockUserMapper); // Используйте мок
    }

    @Test
    @DisplayName("Тест: Конвертация DTO в Notes")
    void testConvertDtoToNotes() {
        NotesDto dto = new NotesDto(1, null, "Test Comment", null);
        Notes entity = notesMapper.convertDtoToNotes(dto);

        Assertions.assertEquals(dto.getId(), entity.getId());
        Assertions.assertEquals(dto.getComments(), entity.getComments());
    }

    @Test
    @DisplayName("Тест: Конвертация Notes в DTO")
    void testConvertNotesToDto() {
        Notes entity = new Notes(1, null, "Test Comment", null);
        NotesDto dto = notesMapper.convertNotesToDto(entity);

        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getComments(), dto.getComments());
    }

    @Test
    @DisplayName("Проверка transferListNotesToDto: корректное преобразование")
    void testTransferListNotesToDto() {
        Notes note = new Notes(1, null, "Comment", null);
        NotesDto expectedNoteDto = new NotesDto(1, null, "Comment", null);

        Mockito.when(mockUserMapper.convertUserToDto(null)).thenReturn(null);

        List<NotesDto> result = notesMapper.transferListNotesToDto(List.of(note));

        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(1, result.size(), "Должен быть один элемент");
        Assertions.assertEquals(expectedNoteDto.getComments(), result.get(0).getComments(), "Элементы должны совпадать");

        Mockito.verify(mockUserMapper, Mockito.times(1)).convertUserToDto(null);
    }

    @Test
    @DisplayName("Проверка transferDtoToListNotes: корректное преобразование")
    void testTransferDtoToListNotes() {
        NotesDto noteDto = new NotesDto(1, null, "Comment", null);
        Notes expectedNote = new Notes(1, null, "Comment", null);

        Mockito.when(mockUserMapper.convertDtoToUser(null)).thenReturn(null);

        List<Notes> result = notesMapper.transferDtoToListNotes(List.of(noteDto));

        Assertions.assertNotNull(result, "Результат не должен быть null");
        Assertions.assertEquals(1, result.size(), "Должен быть один элемент");
        Assertions.assertEquals(expectedNote.getComments(), result.get(0).getComments(), "Элементы должны совпадать");

        Mockito.verify(mockUserMapper, Mockito.times(1)).convertDtoToUser(null);
    }
}