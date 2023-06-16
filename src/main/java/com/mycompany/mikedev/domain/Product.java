package com.mycompany.mikedev.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mikedev.domain.enumeration.Categorie;
import com.mycompany.mikedev.domain.enumeration.Section;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name="price",nullable=false)
    private Long price;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Categorie type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "section", nullable = false)
    private Section section;


    // @OneToMany(mappedBy = "product")
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    // @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    // private Set<ProductPrice> productPrice = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Product image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Product imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Categorie getType() {
        return this.type;
    }

    public Product type(Categorie type) {
        this.setType(type);
        return this;
    }

    public void setType(Categorie type) {
        this.type = type;
    }

    public Section getSection() {
        return this.section;
    }

    public Product section(Section section) {
        this.setSection(section);
        return this;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", type='" + getType() + "'" +
            ", section='" + getSection() + "'" +
            "}";
    }

    // /**
    //  * @return Set<ProductPrice> return the productPrice
    //  */
    // public Set<ProductPrice> getProductPrice() {
    //     return productPrice;
    // }

    // /**
    //  * @param productPrice the productPrice to set
    //  */
    // public void setProductPrice(Set<ProductPrice> productPrice) {
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
