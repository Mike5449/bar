package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.service.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Spring Data JPA repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {



    @Query("SELECT e FROM Employee e JOIN e.attendance a WHERE e.job.jobName=:job AND DATE_FORMAT(a.date,'%Y-%m-%d')=:today")
    List<Employee> findPresentEmployee(@Param("today") String today , @Param("job") String job);

    @Query("SELECT e FROM Employee e JOIN e.attendance a WHERE e.id=:idEmployee AND DATE_FORMAT(a.date,'%Y-%m-%d')=:today")
    Optional<Employee> findPresentEmployeeExist(@Param("today") String today , @Param("idEmployee") Long idEmployee);

    Optional<Employee> findByUser_Login(String login);
    default Optional<Employee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Employee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct employee from Employee employee left join fetch employee.job",
        countQuery = "select count(distinct employee) from Employee employee"
    )
    Page<Employee> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct employee from Employee employee left join fetch employee.job")
    List<Employee> findAllWithToOneRelationships();

    @Query("select employee from Employee employee left join fetch employee.job where employee.id =:id")
    Optional<Employee> findOneWithToOneRelationships(@Param("id") Long id);
}