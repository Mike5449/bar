package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.RequestStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.LeaveRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant enDate;

    private RequestStatus requestStatus;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEnDate() {
        return enDate;
    }

    public void setEnDate(Instant enDate) {
        this.enDate = enDate;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
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
        if (!(o instanceof LeaveRequestDTO)) {
            return false;
        }

        LeaveRequestDTO leaveRequestDTO = (LeaveRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leaveRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveRequestDTO{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", enDate='" + getEnDate() + "'" +
            ", requestStatus='" + getRequestStatus() + "'" +
            ", employee=" + getEmployee() +
            "}";
    }
}
