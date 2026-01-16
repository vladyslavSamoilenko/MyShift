package com.example.backend.mapper;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.entities.User;
import com.example.backend.model.request.post.employeeRequests.EmployeeRequest;
import com.example.backend.model.request.post.userRequests.UserOwnerRequest;
import com.example.backend.model.request.post.userRequests.UserUpdateRequest;
import com.example.backend.security.model.profiles.UserProfileDTO;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring", //mapper as a bean
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {DateTimeUtils.class, Object.class}
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "employee.id", target = "employeeId")
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "token", source = "token")
    UserProfileDTO toUserProfileDTO(User user, String token);

    @Mapping(target = "id", ignore = true)
    User createUserOwner(UserOwnerRequest userOwnerRequest);

    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);
}

