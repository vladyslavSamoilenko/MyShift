package com.example.backend.model.request.post.projectRequests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest implements Serializable {

    @NotBlank(message = "Must contains name")
    private String name;
    @NotBlank(message = "must contains description")
    private String description;
}
