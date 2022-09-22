package com.neo.vas.repository;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyDiscountCommission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 19/05/2021
 */
@Repository
public interface AgencyDiscountCommissionRepository extends JpaRepository<AgencyDiscountCommission,Long> {

    @Query("select ad from AgencyDiscountCommission ad where ad.agencyAD.id = :id")
    AgencyDiscountCommission getAgencyDiscount(long id);


//    search agency discount
    @Query("select ad from AgencyDiscountCommission ad where (?1 is null or ad.agencyAD.id = ?1) " +
            " and (?2 is null or ad.discountPolicyAD.id = ?2) and ad.type = ?3 ")
    Page<AgencyDiscountCommission> searchAD(String agencyName, String policyName, int type, Pageable pageable);

    @Query("select ad from AgencyDiscountCommission ad where ad.discountPolicyAD.id = :id")
    List<AgencyDiscountCommission> deleteAgencyDiscount(long id);
}
