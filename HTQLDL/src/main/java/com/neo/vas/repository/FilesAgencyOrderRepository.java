package com.neo.vas.repository;

import com.neo.vas.domain.Files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 07/06/2021
 */
@Repository
public interface FilesAgencyOrderRepository extends JpaRepository<Files,Long> {
    @Query(value ="select * from Files where func_name='AGENCY_ORDERS' and reference_id= ?1", nativeQuery = true)
    List<Files> getFileByOrderId(long orderId);
    
    @Query(value ="select * from Files where func_name='CONTRACT_FILE_SR' and reference_id= ?1", nativeQuery = true)
    List<Files> getContractFileBySrId(long srId);
    
    @Query(value ="select * from Files where func_name='PAY_FILE_SR' and reference_id= ?1", nativeQuery = true)
    List<Files> getPayFileBySrId(long srId);
    
    @Query(value ="select * from Files where func_name='SCAN_FILE_SR' and reference_id= ?1", nativeQuery = true)
    List<Files> getScanFileBySrId(long srId);
}
