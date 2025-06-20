package com.example.backend.model.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {
    @NotBlank(message = "Must contains name")
    private String name;
    @NotBlank(message = "must contains description")
    private String description;
}
