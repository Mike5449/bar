package com.mycompany.mikedev.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.Salary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalaryDTO implements Serializable {

    private Long id;

    private Integer month;

    private Integer year;

    @NotNull
    private Double amount;

    private EmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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
        if (!(o instanceof SalaryDTO)) {
            return false;
        }

        SalaryDTO salaryDTO = (SalaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalaryDTO{" +
            "id=" + getId() +
            ", month=" + getMonth() +
            ", year=" + getYear() +
            ", amount=" + getAmount() +
            ", employee=" + getEmployee() +
            "}";
    }
}
