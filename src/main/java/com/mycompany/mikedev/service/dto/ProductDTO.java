package com.mycompany.mikedev.service.dto;

import com.mycompany.mikedev.domain.enumeration.Categorie;
import com.mycompany.mikedev.domain.enumeration.Section;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.mikedev.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long price;

    @Lob
    private byte[] image;

    private String imageContentType;

    @NotNull
    private Categorie type;

    @NotNull
    private Section section;

    // private Set<ProductPriceDTO> productPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Categorie getType() {
        return type;
    }

    public void setType(Categorie type) {
        this.type = type;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", type='" + getType() + "'" +
            ", section='" + getSection() + "'" +
            "}";
    }

    /**
     * @return Long return the productPrice
     */
    





    // /**
    //  * @return Set<ProductPriceDTO> return the productPrice
    //  */
    // public Set<ProductPriceDTO> getProductPrice() {
    //     return productPrice;
    // }

    // /**
    //  * @param productPrice the productPrice to set
    //  */
    // public void setProductPrice(Set<ProductPriceDTO> productPrice) {
    //     this.productPrice = productPrice;
    // }


    /**
     * @return Long return the price
     */
    public Long getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(Long price) {
        this.price = price;
    }

}
