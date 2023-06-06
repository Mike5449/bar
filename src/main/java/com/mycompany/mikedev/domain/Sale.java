package com.mycompany.mikedev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mikedev.domain.enumeration.StatusVente;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "amount_total")
    private Double amountTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusVente status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "job" }, allowSetters = true)
    private User user;

    @ManyToOne
    private Client client;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Depot depot;

    @ManyToOne
    private Product produit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "produit" }, allowSetters = true)
    private ProductPrice currentPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Sale quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public Sale unitPrice(Double unitPrice) {
        this.setUnitPrice(unitPrice);
        return this;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getAmountTotal() {
        return this.amountTotal;
    }

    public Sale amountTotal(Double amountTotal) {
        this.setAmountTotal(amountTotal);
        return this;
    }

    public void setAmountTotal(Double amountTotal) {
        this.amountTotal = amountTotal;
    }

    public StatusVente getStatus() {
        return this.status;
    }

    public Sale status(StatusVente status) {
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

    public Sale employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Sale client(Client client) {
        this.setClient(client);
        return this;
    }

    public Depot getDepot() {
        return this.depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Sale depot(Depot depot) {
        this.setDepot(depot);
        return this;
    }

    public Product getProduit() {
        return this.produit;
    }

    public void setProduit(Product product) {
        this.produit = product;
    }

    public Sale produit(Product product) {
        this.setProduit(product);
        return this;
    }

    public ProductPrice getCurrentPrice() {
        return this.currentPrice;
    }

    public void setCurrentPrice(ProductPrice productPrice) {
        this.currentPrice = productPrice;
    }

    public Sale currentPrice(ProductPrice productPrice) {
        this.setCurrentPrice(productPrice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sale)) {
            return false;
        }
        return id != null && id.equals(((Sale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sale{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", amountTotal=" + getAmountTotal() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
