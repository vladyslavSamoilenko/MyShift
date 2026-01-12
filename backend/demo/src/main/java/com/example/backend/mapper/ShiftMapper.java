package com.example.backend.mapper;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.dto.ShiftEmployeeInfoDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.entities.ShiftEmployee;
import com.example.backend.model.entities.User;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
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
public abstract class ShiftMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "shiftDate", target = "shiftDate")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "shiftEmployees", target = "employees")
    public abstract ShiftDTO toShiftDTO(Shift shift);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(target = "fullName", expression = "java(getFullName(shiftEmployee))")
    public abstract ShiftEmployeeInfoDTO toShiftEmployeeInfoDTO(ShiftEmployee shiftEmployee);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "shiftDate", target = "shiftDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "startTime", target = "startTime",dateFormat = "HH:mm")
    @Mapping(source = "endTime", target = "endTime",dateFormat = "HH:mm")
    @Mapping(target = "project", expression = "java(toProject(shiftRequest.getProjectId()))")
    @Mapping(target = "user", expression = "java(toUser(shiftRequest.getUserId()))")
    @Mapping(target = "shiftEmployees", ignore = true)
    public abstract Shift toShift(ShiftRequest shiftRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "updateShiftRequest.shiftDate", target = "shiftDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "updateShiftRequest.startTime", target = "startTime",dateFormat = "HH:mm")
    @Mapping(source = "updateShiftRequest.endTime", target = "endTime",dateFormat = "HH:mm")
    public abstract void updateShift(@MappingTarget Shift shift, UpdateShiftRequest updateShiftRequest);

    protected String getFullName(ShiftEmployee se) {
        if (se == null || se.getEmployee() == null) return null;
        return se.getEmployee().getFirstName() + " " + se.getEmployee().getLastName();
    }

    protected Project toProject(Integer id){
        if(id == null) return null;
        Project project = new Project();
        project.setId(id);
        return project;
    }

    protected User toUser(Integer id){
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }
}
