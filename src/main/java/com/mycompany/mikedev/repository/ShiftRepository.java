package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Shift;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Shift entity.
 */
@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    default Optional<Shift> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Shift> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Shift> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct shift from Shift shift left join fetch shift.employee",
        countQuery = "select count(distinct shift) from Shift shift"
    )
    Page<Shift> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct shift from Shift shift left join fetch shift.employee")
    List<Shift> findAllWithToOneRelationships();

    @Query("select shift from Shift shift left join fetch shift.employee where shift.id =:id")
    Optional<Shift> findOneWithToOneRelationships(@Param("id") Long id);
}
