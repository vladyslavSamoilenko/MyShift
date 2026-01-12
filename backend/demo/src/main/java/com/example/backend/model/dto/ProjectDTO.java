package com.example.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private List<ShiftDTO> shifts;
    private boolean isDeleted;

}
