package com.effectiveMobile.effectivemobile.other;

public enum DefaultSettingsFieldNameEnum {

    TASK_PRIORITY("TASK PRIORITY"),
    TASK_STATUS("TASK STATUS");

    private String fieldName;

    DefaultSettingsFieldNameEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
