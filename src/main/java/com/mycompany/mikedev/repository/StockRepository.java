package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Product;
import com.mycompany.mikedev.domain.Stock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stock entity.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    default Optional<Stock> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    Optional<Stock> findByProduct(Product product);

    default List<Stock> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Stock> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct stock from Stock stock left join fetch stock.product",
        countQuery = "select count(distinct stock) from Stock stock"
    )
    Page<Stock> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct stock from Stock stock left join fetch stock.product")
    List<Stock> findAllWithToOneRelationships();

    @Query("select stock from Stock stock left join fetch stock.product where stock.id =:id")
    Optional<Stock> findOneWithToOneRelationships(@Param("id") Long id);
}
