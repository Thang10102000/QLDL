package com.neo.vas.repository;

import com.neo.vas.domain.BrandCommissionPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/26/2021
 */
@Repository
public interface BrandCommissionPolicyRepository extends JpaRepository<BrandCommissionPolicy,Long> {

    @Query("select bcp from BrandCommissionPolicy bcp where (?1 is null or bcp.discountCommissionBC.id  = ?1 ) and (?2 is null or bcp.brandBP.id = ?2 ) ")
    Page<BrandCommissionPolicy> searchBrandCommission(String commissionId, String brandId,  Pageable pageable);

//    delete by commission Id
    @Query("select bcp from BrandCommissionPolicy bcp where bcp.discountCommissionBC.id = ?1 ")
    List<BrandCommissionPolicy> deleteBrandByCId(Long id);

}
