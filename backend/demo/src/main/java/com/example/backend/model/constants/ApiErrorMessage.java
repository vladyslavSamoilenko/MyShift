package com.example.backend.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    PROJECT_NOT_FOUND_BY_ID("Project not found by id: %s"),
    PROJECT_ALREADY_EXIST("Project with Name: %s already exist");

    private final String message;

    public String getMessage(Object ... args){
        return String.format(message, args);
    }
}
