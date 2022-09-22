package com.neo.vas.repository;

import com.neo.vas.domain.AMKAM;
import com.neo.vas.domain.AgencyArea;
import com.neo.vas.domain.AgencyOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Agency;

import java.util.List;

@Repository
public interface AmkamRepository extends JpaRepository<Agency, Long>, JpaSpecificationExecutor<Agency>{
	@Query("select ag from Agency ag where upper(concat(ag.agencyCode)) like %?1% and FN_CONVERT_TO_VN(upper(concat(ag.agencyName))) like %?2% "
			+ " and concat(ag.areaId.areaName) like %?3%  and  ag.status = 0  and ag.agencyType = 1 and ag.type =1 order by ag.createdDate DESC")
	List<Agency> findAmkamEx(String agencyCode, String agencyName, String areaId);

	@Query("select ag from Agency ag where upper(concat(ag.agencyCode)) like %?1% and FN_CONVERT_TO_VN(upper(concat(ag.agencyName))) like %?2% "
			+ " and concat(ag.areaId.areaName) like %?3% and FN_CONVERT_TO_VN(ag.shopCode) like %?4%  and ag.status = 0  and ag.agencyType = 1 and ag.type =1 order by ag.createdDate DESC")
	Page<Agency> findAllAmkam(String agencyCode, String agencyName, String areaId, String shopCode, Pageable pageable);

	@Query("select ag from Agency ag where upper(concat(ag.agencyCode)) like %?1% and FN_CONVERT_TO_VN(upper(concat(ag.agencyName))) like %?2% "
			+ " and concat(ag.areaId.areaName) like %?3% and ag.status = 0  and ag.agencyType = 1 and ag.type =1 order by ag.createdDate DESC")
	Page<Agency> findAmkam(String agencyCode, String agencyName, String areaId, Pageable pageable);
}
