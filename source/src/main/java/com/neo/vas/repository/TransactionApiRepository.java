package com.neo.vas.repository;

import com.neo.vas.domain.ApiTransactionOrderLog;
import com.neo.vas.domain.LogAPI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.ApiTransactionLog;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionApiRepository extends JpaRepository<ApiTransactionLog, Long>{

    
    // TH1 co from và to date
    @Query("select atl from ApiTransactionLog atl where atl.created >= :fromDate and atl.responseTime <= :toDate order by atl.created desc")
    Page<ApiTransactionLog> findAllLogApiFullDate(Date fromDate, Date toDate, Pageable pageable);
    // TH2 khong có end date
    @Query("select atl from ApiTransactionLog atl where atl.created >= :fromDate order by atl.created desc")
    Page<ApiTransactionLog> findAllLogApiFromDate(Date fromDate, Pageable pageable);

    // TH3 khong co start date
    @Query("select atl from ApiTransactionLog atl where atl.responseTime <= :toDate order by atl.created desc")
    Page<ApiTransactionLog> findAllLogApiToDate(Date toDate, Pageable pageable);
    @Query("select atl from ApiTransactionLog atl order by atl.created")
    Page<ApiTransactionLog> findAllNoDate(Pageable pageable);

    @Query("select ao from ApiTransactionLog ao where ao.orderId = ?1 ")
    List<ApiTransactionLog> searchTransactionLogOrder(Long orderId);
}