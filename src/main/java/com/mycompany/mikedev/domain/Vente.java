package com.mycompany.mikedev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mikedev.domain.enumeration.StatusVente;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vente.
 */
@Entity
@Table(name = "vente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusVente status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Employee employee;

    @ManyToOne
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee", "client" }, allowSetters = true)
    private Depot depot;

    @ManyToOne
    private Boisson boisson;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return this.quantite;
    }

    public Vente quantite(Integer quantite) {
        this.setQuantite(quantite);
        return this;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public StatusVente getStatus() {
        return this.status;
    }

    public Vente status(StatusVente status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusVente status) {
        this.status = status;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Vente employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vente client(Client client) {
        this.setClient(client);
        return this;
    }

    public Depot getDepot() {
        return this.depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Vente depot(Depot depot) {
        this.setDepot(depot);
        return this;
    }

    public Boisson getBoisson() {
        return this.boisson;
    }

    public void setBoisson(Boisson boisson) {
        this.boisson = boisson;
    }

    public Vente boisson(Boisson boisson) {
        this.setBoisson(boisson);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vente)) {
            return false;
        }
        return id != null && id.equals(((Vente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vente{" +
            "id=" + getId() +
            ", quantite=" + getQuantite() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
