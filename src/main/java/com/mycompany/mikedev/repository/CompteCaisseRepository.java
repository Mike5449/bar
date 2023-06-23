package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.domain.enumeration.StatusCaisse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CompteCaisse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteCaisseRepository extends JpaRepository<CompteCaisse, Long> {

    @Query("select c from CompteCaisse c where " +
        "(c.employee.id =:employeeId)  and " +
        "status=:status ")
    List<CompteCaisse> findByEmployeeAndCompteActive(@Param("employeeId") Long userAccountId,@Param("status") StatusCaisse status);

    // @Query("update c from CompteCaisse c where " + "(c.id =:idCaisseActive) and " + "c.injection =:injection ")
    // Optional<CompteCaisse> updateInjection(@Param("idCaisseActive") Long idCaisseActive , @Param("injection") Long injection);

    // @Query("update c from CompteCaisse c where " + "(c.id =:idCaisseActive) and " + "c.pret =:pret ")
    // Optional<CompteCaisse> updatePret(@Param("idCaisseActive") Long idCaisseActive , @Param("pret") Long injection);

}
