package com.example.backend.model.dto;

import com.example.backend.model.entities.Shift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private List<Shift> shifts;
    private boolean isDeleted;

}
