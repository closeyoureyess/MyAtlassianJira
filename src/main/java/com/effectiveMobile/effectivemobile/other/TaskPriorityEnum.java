package com.effectiveMobile.effectivemobile.other;

/**
 * <pre>
 *     Enum, описывающий приоритет задач
 * </pre>
 */
public enum TaskPriorityEnum {

    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low");

    private String taskPriority;

    TaskPriorityEnum(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskPriority() {
        return taskPriority;
    }
}
