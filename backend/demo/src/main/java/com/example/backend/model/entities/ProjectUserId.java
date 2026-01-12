package com.example.backend.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ProjectUserId implements Serializable {
    private Integer project;
    private Integer user;

    public ProjectUserId(){};

    public ProjectUserId(Integer project, Integer user){
        this.project = project;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProjectUserId that = (ProjectUserId) o;
        return Objects.equals(project, that.project) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, user);
    }
}
