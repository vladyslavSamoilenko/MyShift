package com.example.backend.model.request.post.userRequests;
import com.example.backend.model.dto.ProjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOwnerRequest {
    private UserDto userData;

    private EmployeeDto employeeData;

    private ProjectDTO projectData;

    @Data
    public static class UserDto {
        private String email;
        private String password;
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
