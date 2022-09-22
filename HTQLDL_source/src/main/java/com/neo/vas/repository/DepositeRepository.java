package com.neo.vas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Deposits;
/**
 * project_name: demo
 * Created_by: dat dep trai
 * time: 27/05/2021
 */
@Repository
public interface DepositeRepository extends JpaRepository<Deposits,Long>,
PagingAndSortingRepository<Deposits,Long>{

//	get deposit amount
	@Query("select d from Deposits d where d.agencyD.id = ?1 ")
	List<Deposits> getDepositByAgencyId(long agencyId);

	// Search if no input null
	@Query("SELECT d FROM Deposits d WHERE ( ?1 is null or d.agencyD.id = ?1 ) AND d.depositsAmount LIKE %?2% AND d.startDate >= ?3 AND d.endDate <= ?4 "
			+ "AND (?5 is null or d.status = ?5 ) order by d.createDate desc ")
	public Page<Deposits> findAll(String agencyId, String depositsAmount,
			Date startDate, Date endDate, String status, Pageable pageable);
	
	// Search if start date is null
		@Query("SELECT d FROM Deposits d WHERE (?1 is null or d.agencyD.id = ?1 )  "
				+ "AND d.depositsAmount LIKE %?2% "
				+ "AND d.endDate <= ?3 "
				+ "AND (?4 is null or d.status = ?4)  order by d.createDate desc "
				)
		public Page<Deposits> findAllNoStartDate(String agencyId, String depositsAmount,
				Date endDate, String status, Pageable pageable);
		
		// Search if end date is null
		@Query("SELECT d FROM Deposits d WHERE (?1 is null or d.agencyD.id = ?1) "
				+ "AND d.depositsAmount LIKE %?2% "
				+ "AND d.startDate >= ?3 "
				+ "AND ( ?4 is null or d.status = ?4)  order by d.createDate desc "
				)
		public Page<Deposits> findAllNoEndDate(String agencyId, String depositsAmount,
				Date endDate, String status, Pageable pageable);
	
	// Search if startDate and endDate is null
	@Query("SELECT d FROM Deposits d WHERE (?1 is null or d.agencyD.id = ?1) "
			+ "AND d.depositsAmount LIKE %?2% "
			+ "AND (?3 is null or d.status = ?3) order by d.createDate desc ")
	 Page<Deposits> findAllNoDate(String agencyId, String depositsAmount, String status, Pageable pageable);

//	check start date, end date deposit
	@Query("select dc from Deposits dc where dc.agencyD.id = ?1 and ((dc.startDate <= ?2 and dc.endDate >= ?3) or (dc.startDate >= ?2 and dc.startDate <= ?3 and dc.endDate >= ?3 ) " +
			" or (dc.startDate <= ?2 and dc.endDate >= ?2 and dc.endDate <= ?3 )) ")
	Deposits getDepositsExist(Long agencyId, Date startDate , Date endDate);

//	list export deposit all date
	@Query("select de from Deposits de where (?1 is null or de.agencyD.id = ?1 ) and (?2 is null or de.depositsAmount = ?2) and (?3 is null or de.startDate >= ?3 ) " +
			" and (?4 is null or de.endDate <= ?4 ) and (?5 is null or de.status = ?5 ) order by de.agencyD.id desc ")
	List<Deposits> exportDepositAllDate(String agencyId, String depositsAmount,Date startDate, Date endDate, String status);

	//	list export deposit no start date
	@Query("select de from Deposits de where (?1 is null or de.agencyD.id = ?1 ) and (?2 is null or de.depositsAmount = ?2) " +
			" and (?3 is null or de.endDate <= ?3 ) and (?4 is null or de.status = ?4 ) order by de.agencyD.id desc ")
	List<Deposits> exportDepositNoStartDate(String agencyId, String depositsAmount, Date endDate, String status);

	//	list export deposit no end date
	@Query("select de from Deposits de where (?1 is null or de.agencyD.id = ?1 ) and (?2 is null or de.depositsAmount = ?2) and (?3 is null or de.startDate >= ?3 ) " +
			" and  (?4 is null or de.status = ?4 ) order by de.agencyD.id desc ")
	List<Deposits> exportDepositNoEndDate(String agencyId, String depositsAmount,Date startDate, String status);
	//	list export deposit no date
	@Query("select de from Deposits de where (?1 is null or de.agencyD.id = ?1 ) and (?2 is null or de.depositsAmount = ?2) and (?3 is null or de.status = ?3 ) order by de.agencyD.id desc")
	List<Deposits> exportDepositNoDate(String agencyId, String depositsAmount, String status);
	

} 
