package com.example.backend.mapper;

import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.Shift;
import com.example.backend.model.request.post.shiftRequests.ShiftRequest;
import com.example.backend.model.request.post.shiftRequests.UpdateShiftRequest;
import com.example.backend.service.ProjectService;
import com.example.backend.service.UserService;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring", //mapper as a bean
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {DateTimeUtils.class, Object.class}
)
public abstract class ShiftMapper {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserService userService;


    @Mapping(source = "id", target = "id")
    @Mapping(source = "shiftDate", target = "shiftDate", dateFormat = "dd-MM-yyyy")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "project.id", target = "projectID")
    @Mapping(source = "user.id", target = "userId")
    public abstract ShiftDTO toShiftDTO(Shift shift);

    @Mapping(source = "id", target = "id", ignore = true)
    @Mapping(source = "shiftDate", target = "shiftDate", dateFormat = "dd-MM-yyyy")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(target = "project", expression = "java(projectService.getById(ShiftRequest.getProjectID()))")
    @Mapping(target = "user", expression = "java(userService.getById(ShiftRequest.getUserId()))")
    public abstract Shift createShift(ShiftRequest shiftRequest);

    @Mapping(target = "id", ignore = true)
    public abstract void updateShift(Shift shift, UpdateShiftRequest updateShiftRequest);
}
