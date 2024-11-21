package com.effectiveMobile.effectivemobile.exeptions;

/**
 * <pre>
 *     Enum с описанием для кастомных ошибок, которые могут выбрасываться в некоторых частях приложения
 * </pre>
 */
public enum DescriptionUserExeption {

    USER_NOT_FOUND("Пользователь не найден. Попробуйте ещё раз"),

    EXECUTOR_NOT_SPECIFIED("Произошла ошибка из-за отсутствующего исполнителя у задачи. Попробуйте ещё раз."),

    NOT_ENOUGH_RULES("Для осуществления действия недостаточно прав на сущность"),
    NOT_ENOUGH_RULES_EXECUTOR("Для осуществления действия недостаточно прав на сущность, исполнитель может редактировать только статус задачи"),
    INCORRECT_TYPE_PARAMETER("В метод передан некорректный тип параметра. Попробуйте снова");

    private String enumDescription;

    DescriptionUserExeption(String enumDescription) {
        this.enumDescription = enumDescription;
    }

    public String getEnumDescription() {
        return enumDescription;
    }
}
