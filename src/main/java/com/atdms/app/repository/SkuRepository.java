package com.atdms.app.repository;

import com.atdms.app.domain.Sku;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Sku entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {

}
