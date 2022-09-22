package com.neo.vas.repository;

import com.neo.vas.domain.Deposits;
import com.neo.vas.dto.AgencyContractDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.AgencyContract;

import java.util.Date;
import java.util.List;

/**
 * project_name: demo
 * Created_by: dat dep trai
 * time: 21/05/2021
 */
@Repository
public interface AgencyContractRepository  extends JpaRepository<AgencyContract,Long>
, PagingAndSortingRepository<AgencyContract,Long> , JpaSpecificationExecutor<AgencyContract> {

	@Query("SELECT c FROM AgencyContract c WHERE CONCAT(c.contractNo) LIKE %?1% "
			+ "AND (?2 is null or c.agencyAC.agencyName = ?2 ) "
			+ "AND (?3 is null or c.status = ?3 ) and ( ?4 is null or c.serviceType = ?4 ) " )
	 Page<AgencyContract> findAll(String contractNo, String agencyName, String status,
			String serviceType, Pageable pageable);

	@Query("select ac from AgencyContract ac where  (?1 is null or ac.contractNo like %?1% ) and (?2 is null or ac.agencyAC.id = ?2) and (?3 is null or ac.status = ?3) " +
			"order by ac.createDate desc ")
	Page<AgencyContract> agencyContractSearch(String contractNo, String id, String status, Pageable pageable);

	//	check start date, end date deposit
	@Query("select ac from AgencyContract ac where ac.agencyAC.id = ?1 and ((ac.startDate <= ?2 and ac.endDate >= ?3) or (ac.startDate >= ?2 and ac.startDate <= ?3 and ac.endDate >= ?3 ) " +
			" or (ac.startDate <= ?2 and ac.endDate >= ?2 and ac.endDate <= ?3 )) ")
	AgencyContract getAgencyContractExist(Long agencyId, Date startDate , Date endDate);

//	TH1
	@Query("select ac from AgencyContract ac where  (?1 is null or ac.contractNo like %?1% ) and (?2 is null or ac.agencyAC.id = ?2) and (?3 is null or ac.status = ?3) " +
			"and (?4 is null or ac.startDate <= ?4) and (?5 is null or ac.endDate >= ?5) order by ac.createDate desc ")
	Page<AgencyContract> searchAgencyContract(String contractNo, String id, String status, Date startDate, Date endDate, Pageable pageable);
//	TH2
	@Query("select ac from AgencyContract ac where  (?1 is null or ac.contractNo like %?1% ) and (?2 is null or ac.agencyAC.id = ?2) and (?3 is null or ac.status = ?3) " +
			"and (?4 is null or ac.startDate <= ?4) order by ac.createDate desc ")
	Page<AgencyContract> searchAgencyContractEndDateNull(String contractNo, String id, String status, Date startDate,  Pageable pageable);

//	TH3
	@Query("select ac from AgencyContract ac where (?1 is null or ac.contractNo like %?1% ) and (?2 is null or ac.agencyAC.id = ?2) and (?3 is null or ac.status = ?3) " +
			" and (?4 is null or ac.endDate >= ?4) order by ac.createDate desc ")
	Page<AgencyContract> searchAgencyContractStartDateNul(String contractNo, String id, String status, Date endDate, Pageable pageable);

//	TH4
	@Query("select ac from AgencyContract ac where  (?1 is null or ac.contractNo like %?1% ) and (?2 is null or ac.agencyAC.id = ?2) and (?3 is null or ac.status = ?3) order by ac.createDate desc ")
	Page<AgencyContract> searchAgencyContractStartEndNull(String contractNo, String id, String status, Pageable pageable);

//	search agency contract by agency_id
	@Query("select ac from AgencyContract ac where (?1 is null or ac.agencyAC.id = ?1) ")
	List<AgencyContract> agencyContractSearch(Long agencyId);
}
