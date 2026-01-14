package com.example.backend.security.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationConstants {
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email";
    public static final String ROLE = "role";
    public static final String UPDATED_AT = "updated_at";
}
