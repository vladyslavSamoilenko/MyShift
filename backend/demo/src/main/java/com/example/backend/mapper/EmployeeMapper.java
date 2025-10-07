package com.example.backend.mapper;


import com.example.backend.model.dto.EmployeeDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.request.post.EmployeeRequest;
import com.example.backend.model.request.post.UpdateEmployeeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface EmployeeMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    EmployeeDTO toEmployeeDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    Employee createEmployee(EmployeeRequest employeeRequest);

    @Mapping(target = "id", ignore = true)
    void updateEmployee(@MappingTarget Employee employee, UpdateEmployeeRequest updateEmployeeRequest);

}
