package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Vente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vente entity.
 */
@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {
    default Optional<Vente> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vente> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vente> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vente from Vente vente left join fetch vente.employee left join fetch vente.client left join fetch vente.depot left join fetch vente.boisson",
        countQuery = "select count(distinct vente) from Vente vente"
    )
    Page<Vente> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct vente from Vente vente left join fetch vente.employee left join fetch vente.client left join fetch vente.depot left join fetch vente.boisson"
    )
    List<Vente> findAllWithToOneRelationships();

    @Query(
        "select vente from Vente vente left join fetch vente.employee left join fetch vente.client left join fetch vente.depot left join fetch vente.boisson where vente.id =:id"
    )
    Optional<Vente> findOneWithToOneRelationships(@Param("id") Long id);
}
