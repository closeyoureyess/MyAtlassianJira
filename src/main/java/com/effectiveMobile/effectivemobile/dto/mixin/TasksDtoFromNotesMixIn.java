package com.effectiveMobile.effectivemobile.dto.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"header", "taskAuthor", "taskExecutor", "description", "taskPriority", "taskStatus", "notesDto"})
public class TasksDtoFromNotesMixIn {
}
