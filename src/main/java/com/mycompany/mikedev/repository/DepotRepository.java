package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Client;
import com.mycompany.mikedev.domain.Depot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Depot entity.
 */
@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {
    default Optional<Depot> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Depot> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Depot> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    List<Depot> findByClient(Client client);

    @Query("SELECT SUM(d.amount) FROM Depot d WHERE d.client=:client")
    Double getTotalDepotsByClient(@Param("client") Client client);

    @Query(value="select depot from Depot depot where"+" depot.client.id=:clientId")
    List<Depot>findByClientId(@Param("clientId") Long clientId);

    @Query(
        value = "select distinct depot from Depot depot left join fetch depot.employee left join fetch depot.client",
        countQuery = "select count(distinct depot) from Depot depot"
    )
    
    Page<Depot> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct depot from Depot depot left join fetch depot.employee left join fetch depot.client")
    List<Depot> findAllWithToOneRelationships();

    @Query("select depot from Depot depot left join fetch depot.employee left join fetch depot.client where depot.id =:id")
    Optional<Depot> findOneWithToOneRelationships(@Param("id") Long id);
}
