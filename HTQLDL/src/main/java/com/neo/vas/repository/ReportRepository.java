package com.neo.vas.repository;

import java.awt.*;
import java.sql.ResultSet;
import java.util.List;

import com.neo.vas.dto.DetailReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Agency;

@Repository
public interface ReportRepository extends JpaRepository<Agency,Long> {
	
	@Query(nativeQuery = true, value = "SELECT * FROM AGENCY")
	List<Agency> getReport();

	@Query(value="select PK_REPORT_DETAIL.REPORT_DETAIL(:psBrandGroup, :psBrand, :piPersonnelName, :psAgencyArea, :psAgency, :pStartDate, :psApprovalDate, :pReportMonth) from dual", nativeQuery = true)
	List<DetailReportDTO> reportDetail(@Param("psBrandGroup") String psBrandGroup, @Param("psBrand") String psBrand, @Param("piPersonnelName") String piPersonnelName, @Param("psAgencyArea") String psAgencyArea,
									   @Param("psAgency") String psAgency, @Param("pStartDate") String pStartDate, @Param("psApprovalDate") String psApprovalDate, @Param("pReportMonth") Integer pReportMonth);
}
