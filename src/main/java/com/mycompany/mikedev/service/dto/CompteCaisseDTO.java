package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.StatusCaisse;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.CompteCaisse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteCaisseDTO implements Serializable {

    private Long id;

    private Long injection;

    private Long sale;

    private Long cancel;

    private Long cash;

    private Long pret;

    private Long balance;

    private Long aVerser;

    private StatusCaisse status;

    private String name;

    private EmployeeDTO employee;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInjection() {
        return injection;
    }

    public void setInjection(Long injection) {
        this.injection = injection;
    }

    public Long getSale() {
        return sale;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public Long getCancel() {
        return cancel;
    }

    public void setCancel(Long cancel) {
        this.cancel = cancel;
    }

    public Long getCash() {
        return cash;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

    public Long getPret() {
        return pret;
    }

    public void setPret(Long pret) {
        this.pret = pret;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getaVerser() {
        return aVerser;
    }

    public void setaVerser(Long aVerser) {
        this.aVerser = aVerser;
    }

    public StatusCaisse getStatus() {
        return status;
    }

    public void setStatus(StatusCaisse status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteCaisseDTO)) {
            return false;
        }

        CompteCaisseDTO compteCaisseDTO = (CompteCaisseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, compteCaisseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteCaisseDTO{" +
            "id=" + getId() +
            ", injection=" + getInjection() +
            ", sale=" + getSale() +
            ", cancel=" + getCancel() +
            ", cash=" + getCash() +
            ", pret=" + getPret() +
            ", balance=" + getBalance() +
            ", aVerser=" + getaVerser() +
            ", status='" + getStatus() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }

    /**
     * @return Long return the aVerser
     */
    public Long getAVerser() {
        return aVerser;
    }

    /**
     * @param aVerser the aVerser to set
     */
    public void setAVerser(Long aVerser) {
        this.aVerser = aVerser;
    }

    /**
     * @return EmployeeDTO return the employee
     */
    public EmployeeDTO getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

}
