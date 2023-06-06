package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.RequestStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.Shift} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShiftDTO implements Serializable {

    private Long id;

    private Instant shiftDate;

    private RequestStatus shiftType;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Instant shiftDate) {
        this.shiftDate = shiftDate;
    }

    public RequestStatus getShiftType() {
        return shiftType;
    }

    public void setShiftType(RequestStatus shiftType) {
        this.shiftType = shiftType;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShiftDTO)) {
            return false;
        }

        ShiftDTO shiftDTO = (ShiftDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shiftDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShiftDTO{" +
            "id=" + getId() +
            ", shiftDate='" + getShiftDate() + "'" +
            ", shiftType='" + getShiftType() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
