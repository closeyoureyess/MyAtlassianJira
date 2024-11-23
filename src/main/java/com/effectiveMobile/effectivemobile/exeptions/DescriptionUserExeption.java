package com.effectiveMobile.effectivemobile.exeptions;

/**
 * <pre>
 *     Enum с описанием для кастомных ошибок, которые могут выбрасываться в некоторых частях приложения
 * </pre>
 */
public enum DescriptionUserExeption {

    GENERATION_ERROR("Возникла ошибка в системе: "),
    ID_TASKS_NOT_BE_NULL("Возникла ошибка в системе: Передаваемый идентификатор задачи не может быть null"),
    TASKS_ENTITY_NOT_BE_NULL("Передаваемый объект задача не может быть null"),
    USER_NOT_FOUND("Пользователь не найден. Попробуйте ещё раз"),
    EXECUTOR_NOT_SPECIFIED("Произошла ошибка из-за отсутствующего исполнителя у задачи. Попробуйте ещё раз."),
    NOT_ENOUGH_RULES_MUST_BE_EXECUTOR(
            "Для редактирования недостаточно прав на сущность. " +
                    "Исполнитель по задаче может редактировать только статус задачи и оставлять комментарии"),
    NOT_ENOUGH_RULES_MUST_BE_ADMIN("Для редактирования недостаточно прав на сущность. " +
            "Для редактирования задач, по которым пользователь не является исполнителем, а также для изменения любых полей в задаче, " +
            "кроме статуса, необходима роль администратора"),
    INCORRECT_TYPE_PARAMETER("В метод передан некорректный тип параметра. Попробуйте снова"),
    TASKS_ENTITY_NOT_FOUND("Задача по заданному идентификатору не найдена. Исправьте id задачи и попробуйте ещё раз");

    private String enumDescription;

    DescriptionUserExeption(String enumDescription) {
        this.enumDescription = enumDescription;
    }

    public String getEnumDescription() {
        return enumDescription;
    }
}
