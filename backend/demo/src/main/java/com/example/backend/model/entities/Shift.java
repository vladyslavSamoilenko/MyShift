package com.example.backend.model.entities;

import com.example.backend.model.enums.Status;
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

    @Column(nullable = false)
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "actual_start_time")
    private LocalDateTime actual_start_time;

    @Column(name = "actual_end_time")
    private LocalDateTime actual_end_time;

    @Column(name = "break_duration")
    private Integer break_duration;

    @Column(name = "break_start")
    private LocalTime break_start;

    @Column(name = "break_end")
    private LocalTime break_end;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }
}
