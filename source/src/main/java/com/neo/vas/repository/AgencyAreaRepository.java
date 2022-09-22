package com.neo.vas.repository;

import com.neo.vas.domain.AgencyArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyAreaRepository extends JpaRepository<AgencyArea,Long> {
    AgencyArea findAgencyAreaByAreaId(Long id);
    @Query("select ag from AgencyArea ag where upper(nvl(ag.areaCode,'1')) like %?1% and FN_CONVERT_TO_VN(upper(concat(ag.areaName))) like %?2% " +
            " and upper(nvl(ag.taxCode,'1')) like %?3% and FN_CONVERT_TO_VN(upper(nvl(ag.description,'1'))) like %?4% order by ag.createDate desc ")
    Page<AgencyArea> findAllArea(String areaCode, String areaName, String taxCode, String description, Pageable pageable);
    AgencyArea findAgencyAreaByAreaCode(String id);
}
