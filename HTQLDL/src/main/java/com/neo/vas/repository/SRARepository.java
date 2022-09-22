package com.neo.vas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neo.vas.domain.ServiceRequestActivate;

public interface SRARepository extends JpaRepository<ServiceRequestActivate, Long>, JpaSpecificationExecutor<ServiceRequestActivate> {
	@Query(value = "select count(sr_id) from service_request_activate where sr_id = :srId", nativeQuery = true)
	Integer checkQuantity(@Param("srId") Long srId);
}
