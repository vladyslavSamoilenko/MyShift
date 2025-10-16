package com.example.backend.mapper;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.entities.User;
import com.example.backend.model.request.post.UserRequest;
import com.example.backend.model.request.post.UserUpdateRequest;
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

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "employee", expression = "java(toEmployee(userRequest.getEmployeeId()))")
    @Mapping(target = "deleted", constant = "false")
    User createUser(UserRequest userRequest);

    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    default Employee toEmployee(Integer employeeId){
        if(employeeId == null) return null;
        Employee e = new Employee();
        e.setId(employeeId);
        return e;
    }
}
