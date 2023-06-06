package com.mycompany.mikedev.repository;

import com.mycompany.mikedev.domain.Boisson;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Boisson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoissonRepository extends JpaRepository<Boisson, Long> {}
