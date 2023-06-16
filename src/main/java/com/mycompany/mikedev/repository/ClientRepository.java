package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Client;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Client entity.
 */
// @SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {



    @Query("select c from Client c where " + "DATE_FORMAT(c.createdDate,'%Y-%m-%d')=:createdDate ")
    List<Client> clientToDay(@Param("createdDate") String createdDate);





}
