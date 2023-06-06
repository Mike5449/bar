package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.ProductPrice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductPrice entity.
 */
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    default Optional<ProductPrice> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductPrice> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductPrice> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct productPrice from ProductPrice productPrice left join fetch productPrice.produit",
        countQuery = "select count(distinct productPrice) from ProductPrice productPrice"
    )
    Page<ProductPrice> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct productPrice from ProductPrice productPrice left join fetch productPrice.produit")
    List<ProductPrice> findAllWithToOneRelationships();

    @Query("select productPrice from ProductPrice productPrice left join fetch productPrice.produit where productPrice.id =:id")
    Optional<ProductPrice> findOneWithToOneRelationships(@Param("id") Long id);
}
