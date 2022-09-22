package com.neo.vas.repository;

import com.neo.vas.domain.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;


/**
 * project_name: demo
 * Created_by: thulv
 * time: 13/05/2021
 */
@Repository
public interface ServiceRepository extends JpaRepository<Services, Long>, JpaSpecificationExecutor<Services> {
    Services findServicesById(Long id);

    @Query("select bg.servicesBG from BrandGroup bg where bg.id = ?1")
    Services findServicesByBG(Long id);
}
