package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.StatusVente;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.Sale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SaleDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    private Double unitPrice;

    private Double amountTotal;

    private StatusVente status;

    private EmployeeDTO employee;

    private ClientDTO client;

    // private DepotDTO depot;

    private ProductDTO product;

    private ProductPriceDTO currentPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(Double amountTotal) {
        this.amountTotal = amountTotal;
    }

    public StatusVente getStatus() {
        return status;
    }

    public void setStatus(StatusVente status) {
        this.status = status;
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

    // public DepotDTO getDepot() {
    //     return depot;
    // }

    // public void setDepot(DepotDTO depot) {
    //     this.depot = depot;
    // }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public ProductPriceDTO getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ProductPriceDTO currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaleDTO)) {
            return false;
        }

        SaleDTO saleDTO = (SaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, saleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SaleDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", amountTotal=" + getAmountTotal() +
            ", status='" + getStatus() + "'" +
            ", employee=" + getEmployee() +
            ", client=" + getClient() +
            // ", depot=" + getDepot() +
            ", product=" + getProduct() +
            ", currentPrice=" + getCurrentPrice() +
            "}";
    }
}
