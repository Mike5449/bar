package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Sale;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sale entity.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // List<Sale> findAllByEmploye_Id(Long employeId);

    default Optional<Sale> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Sale> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Sale> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query("select s from Sale s where " +
        "(s.employee.id=:employeeId)  and " +
        "DATE_FORMAT(s.createdDate,'%Y-%m-%d')=:createdDate ")
    List<Sale> prendreParDateAndEmploy(@Param("employeeId") Long userAccountId,@Param("createdDate") String createdDate);

    @Query("select s from Sale s where " + "DATE_FORMAT(s.createdDate,'%Y-%m-%d')=:createdDate ")
    List<Sale> prendreVenteAujourdhuit(@Param("createdDate") String createdDate);


    @Query(
        value = "select distinct sale from Sale sale left join fetch sale.employee left join fetch sale.client left join fetch sale.depot left join fetch sale.product",
        countQuery = "select count(distinct sale) from Sale sale"
    )
    Page<Sale> findAllWithToOneRelationships(Pageable pageable); 

    @Query(
        "select distinct sale from Sale sale left join fetch sale.employee left join fetch sale.client left join fetch sale.depot left join fetch sale.product"
    )
    List<Sale> findAllWithToOneRelationships();

    @Query(
        "select sale from Sale sale left join fetch sale.employee left join fetch sale.client left join fetch sale.depot left join fetch sale.product where sale.id =:id"
    )
    Optional<Sale> findOneWithToOneRelationships(@Param("id") Long id);

}
