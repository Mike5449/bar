package com.mycompany.mikedev.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.Stock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double buyPrice;

    @NotNull
    private Integer quantityMinimal;

    private ProductDTO product;

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

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Integer getQuantityMinimal() {
        return quantityMinimal;
    }

    public void setQuantityMinimal(Integer quantityMinimal) {
        this.quantityMinimal = quantityMinimal;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockDTO)) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", buyPrice=" + getBuyPrice() +
            ", quantityMinimal=" + getQuantityMinimal() +
            ", product=" + getProduct() +
            "}";
    }
}
