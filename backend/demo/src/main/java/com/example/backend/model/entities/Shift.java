package com.example.backend.model.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "shifts", schema = "my_shift")
public class Shift {

    public static final String SHIFT_DATE_FIELD = "shiftDate";
    public static final String PROJECT_ID_FIELD = "projectId";
    public static final String USER_ID_FIELD = "userId";
    public static final String ID_SHIFT = "id";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate shiftDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shift", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ShiftEmployee> shiftEmployees;

    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
