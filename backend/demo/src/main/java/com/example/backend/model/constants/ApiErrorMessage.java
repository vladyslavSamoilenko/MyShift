package com.example.backend.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiErrorMessage {
    PROJECT_NOT_FOUND_BY_ID("Project not found by id: %s"),
    PROJECT_ALREADY_EXIST("Project with Name: %s already exist"),

    EMPLOYEE_NOT_FOUND_BY_ID("Employee not found by id: %s"),
    EMPLOYEE_ALREADY_EXIST_BY_PHONE("Employee with phone: %s already exist"),
    EMPLOYEE_ALREADY_EXIST_BY_EMAIL("Employee with email: %s already exist"),

    USER_NOT_FOUND_BY_ID("User not found by id: %s"),
    USER_ALREADY_EXISTS_BY_EMAIL("User not found by id: %s"),

    SHIFT_NOT_FOUND_BY_ID("Shift not found by id: %s"),
    SHIFT_NOT_FOUND_BY_USER_ID("Shift not found by id: %s"),
    SHIFT_NOT_FOUND_BY_DAY("Shift not found by day"),
    SHIFT_NOT_FOUND_BY_PROJECT_ID("Shift not found by project id"),

    SHIFT_ALREADY_EXISTS_AT_THIS_DAY_FOR_USER_ID("Shift already exist at this day for user: %s");


    private final String message;

    public String getMessage(Object ... args){
        return String.format(message, args);
    }
}
