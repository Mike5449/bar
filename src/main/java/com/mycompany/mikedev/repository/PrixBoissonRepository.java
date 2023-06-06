package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.PrixBoisson;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrixBoisson entity.
 */
@Repository
public interface PrixBoissonRepository extends JpaRepository<PrixBoisson, Long> {
    default Optional<PrixBoisson> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PrixBoisson> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PrixBoisson> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct prixBoisson from PrixBoisson prixBoisson left join fetch prixBoisson.boisson",
        countQuery = "select count(distinct prixBoisson) from PrixBoisson prixBoisson"
    )
    Page<PrixBoisson> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct prixBoisson from PrixBoisson prixBoisson left join fetch prixBoisson.boisson")
    List<PrixBoisson> findAllWithToOneRelationships();

    @Query("select prixBoisson from PrixBoisson prixBoisson left join fetch prixBoisson.boisson where prixBoisson.id =:id")
    Optional<PrixBoisson> findOneWithToOneRelationships(@Param("id") Long id);
}
