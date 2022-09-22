package com.neo.vas.repository;

import com.neo.vas.domain.ApiTransactionLog;
import com.neo.vas.domain.LogAPI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ApiLogRepository extends JpaRepository<LogAPI, Long> {
    // TH1 co start và end date
    @Query("select al from LogAPI al where (:alId is null or al.alId = :alId ) and (:module is null or al.module.sfId = :module) " +
            "and al.created >= :startDate and  al.created <= :endDate and (:creator is null or al.creator like %:creator%) and (:referenceId is null or al.referenceId = :referenceId ) order by al.created desc ")
    Page<LogAPI> findAllLogApi(Long alId, Long module,
                               Date startDate, Date endDate, String creator, Long referenceId, Pageable pageable);
    // TH2 khong có end date
    @Query("select al from LogAPI al where (:alId is null or al.alId = :alId) and (:module is null or al.module.sfId = :module) " +
            "and ( al.created >= :startDate) and (:creator is null or al.creator like %:creator%) and (:referenceId is null or al.referenceId = :referenceId ) order by al.created desc ")
    Page<LogAPI> findALlLogApiStart(Long alId, Long module, Date startDate, String creator, Long referenceId, Pageable pageable);

    // TH3 khong co start date
    @Query("select al from LogAPI al where (:alId is null or al.alId = :alId) and (:module is null or al.module.sfId = :module) " +
            "and ( al.created <= :endDate) and (:creator is null or al.creator like %:creator%) and (:referenceId is null or al.referenceId = :referenceId ) order by al.created desc ")
    Page<LogAPI> findAllLogApoEnd(Long alId, Long module, Date endDate, String creator, Long referenceId, Pageable pageable);

    // TH4 khong co date
    @Query("select al from LogAPI al where (:alId is null or al.alId = :alId) and (:module is null or al.module.sfId = :module) " +
            "and (:creator is null or al.creator like %:creator%) and (:referenceId is null or al.referenceId = :referenceId ) order by al.created desc ")
    Page<LogAPI> findAllLogApiNoDate(Long alId, Long module, String creator, Long referenceId, Pageable pageable);

    @Query("select ao from LogAPI ao where ao.referenceId = ?1 ")
    List<LogAPI> searchApiLogOrder(Long orderId);
}
