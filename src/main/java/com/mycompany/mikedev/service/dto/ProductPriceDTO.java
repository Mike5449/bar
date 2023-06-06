package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.StatusPrice;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.ProductPrice} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductPriceDTO implements Serializable {

    private Long id;

    @NotNull
    private Float price;

    private StatusPrice status;

    private ProductDTO produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public StatusPrice getStatus() {
        return status;
    }

    public void setStatus(StatusPrice status) {
        this.status = status;
    }

    public ProductDTO getProduit() {
        return produit;
    }

    public void setProduit(ProductDTO produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductPriceDTO)) {
            return false;
        }

        ProductPriceDTO productPriceDTO = (ProductPriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productPriceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductPriceDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            ", produit=" + getProduit() +
            "}";
    }
}
