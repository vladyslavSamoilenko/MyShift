package com.example.backend.mapper;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.entities.User;
import com.example.backend.model.request.post.EmployeeRequest;
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
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "employee.id", target = "employeeId")
    UserDTO toUserDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee", expression = "java(createEmployeeFromId(UserDTO.getEmployeeId()))")
    User createUser(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    default Employee createEmployeeFromId(Integer id){
        if (id == null) return null;
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
