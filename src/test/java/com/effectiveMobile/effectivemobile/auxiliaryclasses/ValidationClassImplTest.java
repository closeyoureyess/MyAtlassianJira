package com.effectiveMobile.effectivemobile.auxiliaryclasses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ValidationClassImplTest {

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит только числа (валидный ID)
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит только числа, возвращает валидный ID")
    public void testValidEmailOrId_WithValidId() {
        // Arrange
        String input = "12345";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertTrue(
                result.isPresent(),
                "Результат должен быть присутствующим Optional"
        );
        Assertions.assertEquals(
                Integer.valueOf(12345),
                result.get().getValidationInteger(),
                "validationInteger должен быть установлен в 12345"
        );
        Assertions.assertNull(
                result.get().getValidationString(),
                "validationString должен оставаться null"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит валидный email
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит валидный email, возвращает валидный email")
    public void testValidEmailOrId_WithValidEmail() {
        // Arrange
        String input = "user@example.com";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertTrue(
                result.isPresent(),
                "Результат должен быть присутствующим Optional"
        );
        Assertions.assertEquals(
                "user@example.com",
                result.get().getValidationString(),
                "validationString должен быть установлен в 'user@example.com'"
        );
        Assertions.assertNull(
                result.get().getValidationInteger(),
                "validationInteger должен оставаться null"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка не является ни валидным ID, ни валидным email
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка не валидна, возвращает Optional.empty")
    public void testValidEmailOrId_WithInvalidInput() {
        // Arrange
        String input = "invalid_input";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertFalse(
                result.isPresent(),
                "Результат должен быть Optional.empty"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка пустая
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка пустая, возвращает Optional.empty")
    public void testValidEmailOrId_WithEmptyString() {
        // Arrange
        String input = "";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertFalse(
                result.isPresent(),
                "Результат должен быть Optional.empty для пустой строки"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит числа и буквы
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит числа и буквы, возвращает Optional.empty")
    public void testValidEmailOrId_WithAlphanumericString() {
        // Arrange
        String input = "123abc";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertFalse(
                result.isPresent(),
                "Результат должен быть Optional.empty для строк с числами и буквами"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит валидный email с поддоменом
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит валидный email с поддоменом, возвращает валидный email")
    public void testValidEmailOrId_WithValidEmailWithSubdomain() {
        // Arrange
        String input = "user@mail.example.com";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertTrue(
                result.isPresent(),
                "Результат должен быть присутствующим Optional"
        );
        Assertions.assertEquals(
                "user@mail.example.com",
                result.get().getValidationString(),
                "validationString должен быть установлен в 'user@mail.example.com'"
        );
        Assertions.assertNull(
                result.get().getValidationInteger(),
                "validationInteger должен оставаться null"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит отрицательное число
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит отрицательное число, возвращает валидный ID")
    public void testValidEmailOrId_WithNegativeNumber() {
        // Arrange
        String input = "-9876";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertTrue(
                result.isPresent(),
                "Результат должен быть присутствующим Optional"
        );
        Assertions.assertEquals(
                Integer.valueOf(-9876),
                result.get().getValidationInteger(),
                "validationInteger должен быть установлен в -9876"
        );
        Assertions.assertNull(
                result.get().getValidationString(),
                "validationString должен оставаться null"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит ноль
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит ноль, возвращает валидный ID")
    public void testValidEmailOrId_WithZero() {
        // Arrange
        String input = "0";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertTrue(
                result.isPresent(),
                "Результат должен быть присутствующим Optional"
        );
        Assertions.assertEquals(
                Integer.valueOf(0),
                result.get().getValidationInteger(),
                "validationInteger должен быть установлен в 0"
        );
        Assertions.assertNull(
                result.get().getValidationString(),
                "validationString должен оставаться null"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит email без домена верхнего уровня
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит email без домена верхнего уровня, возвращает Optional.empty")
    public void testValidEmailOrId_WithInvalidEmail_NoTopLevelDomain() {
        // Arrange
        String input = "user@example";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertFalse(
                result.isPresent(),
                "Результат должен быть Optional.empty для email без домена верхнего уровня"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит email с недопустимыми символами
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит email с недопустимыми символами, возвращает Optional.empty")
    public void testValidEmailOrId_WithInvalidEmail_SpecialCharacters() {
        // Arrange
        String input = "user@exa!mple.com";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        org.junit.jupiter.api.Assertions.assertFalse(
                result.isPresent(),
                "Результат должен быть Optional.empty для email с недопустимыми символами"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит только пробелы
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит только пробелы, возвращает Optional.empty")
    public void testValidEmailOrId_WithOnlySpaces() {
        // Arrange
        String input = "     ";
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act
        Optional<ValidationClassImpl> result = validation.validEmailOrId(input);

        // Assert
        Assertions.assertFalse(
                result.isPresent(),
                "Результат должен быть Optional.empty для строк, содержащих только пробелы"
        );
    }

    /**
     * Тест для метода validEmailOrId
     * Сценарий: Входная строка содержит null (ожидается NullPointerException)
     */
    @Test
    @DisplayName("validEmailOrId: Входная строка содержит null, выбрасывает NullPointerException")
    public void testValidEmailOrId_WithNullInput() {
        // Arrange
        String input = null;
        ValidationClassImpl validation = new ValidationClassImpl();

        // Act & Assert
        Assertions.assertThrows(
                NullPointerException.class,
                () -> validation.validEmailOrId(input),
                "Должен быть выброшен NullPointerException при передаче null"
        );
    }
}
