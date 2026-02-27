package com.example.backend.model.request.post.userRequests;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserOwnerRequest implements Serializable {
    private UserDto userData;

    private EmployeeDto employeeData;

    private ProjectDTO projectData;

    @Data
    public static class UserDto {
        private String email;
        private String password;
        private String confirmPassword;
    }

    @Data
    public static class EmployeeDto {
        private String firstName;
        private String lastName;
        private String phone;
        private String role;
    }

    @Data
    public static class ProjectDTO{
        private String name;
        private String description;
    }
}
