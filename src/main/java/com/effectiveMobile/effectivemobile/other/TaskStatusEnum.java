package com.effectiveMobile.effectivemobile.other;

/**
 * <pre>
 *     Enum, описывающий статус задач
 * </pre>
 */
public enum TaskStatusEnum {

    BACKLOG("BACKLOG"),
    PENDING("PENDING"),
    IN_PROGRESS("IN_PROGRESS"),
    COMPLETED("COMPLETED");

    private String taskStatus;

    TaskStatusEnum(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }
}
