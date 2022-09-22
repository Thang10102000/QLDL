package com.neo.vas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.ImageDeposits;

import java.util.List;

@Repository
public interface ImgDepositsRepository  extends JpaRepository<ImageDeposits,Long>{
    @Query("select img from ImageDeposits img where img.depositsID.id = ?1 ")
    List<ImageDeposits> getAllFileDeposit(Long depositId);

    @Query("select img from ImageDeposits img where img.agencyContractID.id = ?1 ")
    List<ImageDeposits> getAllFileAgencyContractId(Long id);
}
