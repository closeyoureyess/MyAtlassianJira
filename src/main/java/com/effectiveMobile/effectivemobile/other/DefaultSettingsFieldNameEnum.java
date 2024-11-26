package com.effectiveMobile.effectivemobile.other;

public enum DefaultSettingsFieldNameEnum {

    TASK_PRIORITY("TASK_PRIORITY"),
    TASK_STATUS("TASK_STATUS");

    private String fieldName;

    DefaultSettingsFieldNameEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
