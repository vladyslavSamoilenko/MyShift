package com.example.backend.mapper;


import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.request.post.ProjectRequest;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring", //mapper as a bean
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {DateTimeUtils.class, Object.class}
)
public interface ProjectMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    ProjectDTO toProjectDTO(Project project);


    @Mapping(target = "id", ignore = true)
    Project createProject(ProjectRequest projectRequest);
}
