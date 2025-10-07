package com.example.backend.model.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ApiLogMessage {
    PROJECT_INFO_BY_ID("Receiving project with ID: {}"),
    NAME_OF_CURRENT_METHOD("Current method: {}"),
    EMPLOYEE_INFO_BY_ID("Receiving employee with ID: {}")
    ;

    private final String message;
}
