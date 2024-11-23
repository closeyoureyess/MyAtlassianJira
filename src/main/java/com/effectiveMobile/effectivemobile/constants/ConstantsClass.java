package com.effectiveMobile.effectivemobile.constants;

/**
 * <pre>
 *     Класс с константами
 * </pre>
 */
public class ConstantsClass {

    public static final String ADMINROLE = "ADMIN";
    public static final String USERROLE = "USER";
    public static final String SECRETKEY = "F510C4B0354B33E8ED73016958603787042439D41EC31038150B1FA6986B6AA532415E9E6DCF1A60A3354F14CAA6CB2EBF573130E104FF296BDF20ED65D4BC4B";
    public static final String NOTBLANK_EXCEPTION = "Недопустимая попытка передать пустым параметр: ";
    public static final String LINE_FEED = "\n";
    public static final String EMPTY_SPACE = " ";
    public static final String HIDE = "####information is hidden";
    public static final String IS_DELETE = "Сущность была удалена";
    public static final String REGEX_ONLY_NUMBERS = "^-?\\d+$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    //DefaultSettings Controller
    public static final String PUT_CHANGE_DEFAULTSETTINGS = "PutChangeDefaultSettings";

    //Entrance Controller
    public static final String POST_CREATE_USER = "PostCreateUser";
    public static final String POST_AUTHORIZATION_USER = "PostAuthorizationUser";

    //Notes Controller
    public static final String POST_CREATE_NOTES = "PostCreateNotes";

    //Tasks Controller
    public static final String POST_CREATE_TASKS = "PostCreateTasks";
    public static final String GET_TASKAUTHOR_TASKS = "GetGetTaskAuthor";
    public static final String GET_TASKEXECUTOR_TASKS = "GetGetTaskExecutor";
    public static final String PUT_EDIT_TASKS = "PutEditTask";
    public static final String DELETE_TASKS = "DeleteTask";
    public static final Integer ONE_FLAG = 1;
    public static final Integer ZERO_FLAG = 0;
}
