package com.example.backend.mapper;


import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.request.post.projectRequests.ProjectRequest;
import com.example.backend.model.request.post.projectRequests.UpdateProjectRequest;
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
public interface ProjectMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    ProjectDTO toProjectDTO(Project project);


    @Mapping(target = "id", ignore = true)
    Project createProject(ProjectRequest projectRequest);

    @Mapping(target = "id", ignore = true)
    void updateProject(@MappingTarget Project project, UpdateProjectRequest request);
}
