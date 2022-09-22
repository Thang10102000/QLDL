package com.neo.vas.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyArea;

/**
 * project_name: demo Created_by: thulv time: 19/05/2021
 */
@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long>, JpaSpecificationExecutor<Agency> {
//    lấy đại lý, am/kam chưa thuộc chính sách theo areaId
	@Query("select a from Agency a where a.areaId in (:id) and a.id not in ( select adc.agencyAD.id from AgencyDiscountCommission adc ) ")
	List<Agency> getAgencyByAgencyAreaList(List<AgencyArea> id);

    // lấy danh sách đại lý chưa thuộc chính sách theo area id
    @Query("select ag from Agency ag where ag.areaId in (:idArea) and ag.type = 0 and ag.id not in ( select adc.agencyAD.id from AgencyDiscountCommission adc ) ")
    List<Agency> getAgencyByAreaNotPolicy(List<AgencyArea> idArea);

    List<Agency> findAgencyByType(int type);

    Agency findById(long id);

    @Query("select ag from Agency ag where ag.id = ?1 ")
    Agency getAgency(Long agencyId);
// lấy danh sách đại lý theo area id
    @Query("select ag from Agency ag where ag.areaId in (:idArea) and ag.type = 0 ")
    List<Agency> getAgencyByArea(List<AgencyArea> idArea);
    
    @Query("select ag from Agency ag where ag.areaId in (:idArea) and ag.status = 0 ")
    List<Agency> getAgencyAmkamByArea(List<AgencyArea> idArea);

    @Query("select max(ag.id) from Agency ag")
    Long getMaxAgencyId();

    @Query("select ag from Agency ag where lower(ag.agencyName) like %?1% and (?2 is null or lower(ag.areaId.areaId) = ?2) " +
            "and  (?3 is null or ag.status = ?3)  and (?4 is null or ag.type = ?4) order by ag.createdDate desc ")
    Page<Agency> findAllAgency(String agencyName, String areaId, String status, String type, Pageable pageable);

//    load agency commission
//    @Query("select ag from Agency ag where ag.agencyName")

    @Query("select a from Agency a where a.areaId = :areaId and a.type = 0 and a.status = 0")
	List<Agency> getAgencyByArea(Long areaId);

//    lấy đại lý chưa thuộc chính sách hoa hồng và chính sách chiết khấu
    @Query("select ag from Agency ag where ag.type= 0 and ag.id not in ( select adc.agencyAD.id from AgencyDiscountCommission adc )")
    List<Agency> getAgencyNotPolicy();

    //    lấy đại lý,am/kam chưa thuộc chính sách hoa hồng và chính sách chiết khấu
    @Query("select ag from Agency ag where ag.id not in ( select adc.agencyAD.id from AgencyDiscountCommission adc )")
    List<Agency> getAgencyAmKamNotPolicy();

    Agency findAgencyByAgencyCode(String agencyCode);

    @Query("select ag from Agency ag where ag.areaId.areaId = ?1 ")
    List<Agency> getAllAgencyAreaId(Long areaId);
}
