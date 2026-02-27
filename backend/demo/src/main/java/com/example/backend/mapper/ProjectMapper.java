package com.example.backend.mapper;


import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.dto.ShiftDTO;
import com.example.backend.model.entities.Project;
import com.example.backend.model.request.post.projectRequests.UpdateProjectRequest;
import com.example.backend.model.request.post.userRequests.RegisterUserOwnerRequest;
import org.hibernate.type.descriptor.DateTimeUtils;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {DateTimeUtils.class, Object.class},
        uses = {ShiftMapper.class}
)
public abstract class ProjectMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
   public abstract ProjectDTO toProjectDTO(Project project);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "projectData", target = ".")
    public abstract Project createProject(RegisterUserOwnerRequest registerUserOwnerRequest);

    @Mapping(target = "id", ignore = true)
    public abstract void updateProject(@MappingTarget Project project, UpdateProjectRequest request);

    @AfterMapping
    protected void addProjectIdToDTO(@MappingTarget ProjectDTO projectDTO){
        if(projectDTO.getShifts() != null && projectDTO.getId()!= null){
            for(ShiftDTO shiftDTO: projectDTO.getShifts()){
                shiftDTO.setProjectId(projectDTO.getId());
            }
        }
    }
}
