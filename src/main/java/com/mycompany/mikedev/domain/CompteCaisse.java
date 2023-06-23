package com.mycompany.mikedev.domain;

import com.mycompany.mikedev.domain.enumeration.StatusCaisse;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CompteCaisse.
 */
@Entity
@Table(name = "compte_caisse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompteCaisse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Employee employee;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "injection")
    private Long injection;

    @NotNull
    @Column(name = "sale")
    private Long sale;

    @NotNull
    @Column(name = "cancel")
    private Long cancel;

    @NotNull
    @Column(name = "cash")
    private Long cash;

    @NotNull
    @Column(name = "pret")
    private Long pret;

    @NotNull
    @Column(name = "balance")
    private Long balance;

    @NotNull
    @Column(name = "a_verser")
    private Long aVerser;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusCaisse status;

    @Column(name = "name")
    private String name;

    // @PostUpdate
    // public void aVerserAuto(){
    //     setAVerser(getCash()+getBalance());
    // }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompteCaisse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInjection() {
        return this.injection;
    }

    public CompteCaisse injection(Long injection) {
        this.setInjection(injection);
        return this;
    }

    public void setInjection(Long injection) {
        this.injection = injection;
    }

    public Long getSale() {
        return this.sale;
    }

    public CompteCaisse sale(Long sale) {
        this.setSale(sale);
        return this;
    }

    public void setSale(Long sale) {
        this.sale = sale;
    }

    public Long getCancel() {
        return this.cancel;
    }

    public CompteCaisse cancel(Long cancel) {
        this.setCancel(cancel);
        return this;
    }

    public void setCancel(Long cancel) {
        this.cancel = cancel;
    }

    public Long getCash() {
        return this.cash;
    }

    public CompteCaisse cash(Long cash) {
        this.setCash(cash);
        return this;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

    public Long getPret() {
        return this.pret;
    }

    public CompteCaisse pret(Long pret) {
        this.setPret(pret);
        return this;
    }

    public void setPret(Long pret) {
        this.pret = pret;
    }

    public Long getBalance() {
        return this.balance;
    }

    public CompteCaisse balance(Long balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getaVerser() {
        return this.aVerser;
    }

    public CompteCaisse aVerser(Long aVerser) {
        this.setaVerser(aVerser);
        return this;
    }

    public void setaVerser(Long aVerser) {
        this.aVerser = aVerser;
    }

    public StatusCaisse getStatus() {
        return this.status;
    }

    public CompteCaisse status(StatusCaisse status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusCaisse status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public CompteCaisse name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompteCaisse)) {
            return false;
        }
        return id != null && id.equals(((CompteCaisse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompteCaisse{" +
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
     * @return Employee return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
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

}
