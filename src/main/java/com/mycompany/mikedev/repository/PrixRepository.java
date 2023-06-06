package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Prix;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Prix entity.
 */
@Repository
public interface PrixRepository extends JpaRepository<Prix, Long> {
    default Optional<Prix> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Prix> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Prix> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct prix from Prix prix left join fetch prix.boisson",
        countQuery = "select count(distinct prix) from Prix prix"
    )
    Page<Prix> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct prix from Prix prix left join fetch prix.boisson")
    List<Prix> findAllWithToOneRelationships();

    @Query("select prix from Prix prix left join fetch prix.boisson where prix.id =:id")
    Optional<Prix> findOneWithToOneRelationships(@Param("id") Long id);
}
