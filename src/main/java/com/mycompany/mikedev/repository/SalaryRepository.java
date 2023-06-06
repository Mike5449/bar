package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Salary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Salary entity.
 */
@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    default Optional<Salary> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Salary> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Salary> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct salary from Salary salary left join fetch salary.employee",
        countQuery = "select count(distinct salary) from Salary salary"
    )
    Page<Salary> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct salary from Salary salary left join fetch salary.employee")
    List<Salary> findAllWithToOneRelationships();

    @Query("select salary from Salary salary left join fetch salary.employee where salary.id =:id")
    Optional<Salary> findOneWithToOneRelationships(@Param("id") Long id);
}
