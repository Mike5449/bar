package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Attendance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Attendance entity.
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    default Optional<Attendance> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Attendance> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Attendance> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct attendance from Attendance attendance left join fetch attendance.employee",
        countQuery = "select count(distinct attendance) from Attendance attendance"
    )
    Page<Attendance> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct attendance from Attendance attendance left join fetch attendance.employee")
    List<Attendance> findAllWithToOneRelationships();

    @Query("select attendance from Attendance attendance left join fetch attendance.employee where attendance.id =:id")
    Optional<Attendance> findOneWithToOneRelationships(@Param("id") Long id);
}
