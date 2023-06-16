package com.mycompany.mikedev.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.Depot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepotDTO implements Serializable {

    private Long id;

    @NotNull
    private Float amount;

    private String description;

    private EmployeeDTO employee;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepotDTO)) {
            return false;
        }

        DepotDTO depotDTO = (DepotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, depotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepotDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", description='" + getDescription() + "'" +
            ", employee=" + getEmployee() +
            ", client=" + getClient() +
            "}";
    }
}
