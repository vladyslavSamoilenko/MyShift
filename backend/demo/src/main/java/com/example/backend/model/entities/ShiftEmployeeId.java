package com.example.backend.model.entities;

import java.io.Serializable;
import java.util.Objects;

public class ShiftEmployeeId implements Serializable {
    private Integer shift;
    private Integer employee;

    public ShiftEmployeeId() {}

    public ShiftEmployeeId(Integer shift, Integer employee) {
        this.shift = shift;
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShiftEmployeeId that = (ShiftEmployeeId) o;
        return Objects.equals(shift, that.shift) && Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shift, employee);
    }
}
