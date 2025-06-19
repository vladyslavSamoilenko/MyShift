package com.example.backend.controller;


import com.example.backend.model.constants.ApiLogMessage;
import com.example.backend.model.dto.ProjectDTO;
import com.example.backend.model.request.post.ProjectRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.ProjectRepository;
import com.example.backend.service.ProjectService;
import com.example.backend.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectsController {

    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<ProjectDTO>> getProjectById(@PathVariable(name = "id") Integer projectId){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<ProjectDTO> response = projectService.getById(projectId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<ProjectDTO>> createProject(@RequestBody ProjectRequest projectRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<ProjectDTO> response = projectService.createProject(projectRequest);
        return ResponseEntity.ok(response);
    }
}
