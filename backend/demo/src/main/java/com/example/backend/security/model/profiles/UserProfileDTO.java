package com.example.backend.security.model.profiles;

import com.example.backend.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserProfileDTO implements Serializable {

    private Integer id;
    private String email;
    private Role role;
    private String token;

}
