package com.neo.vas.repository;

import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.domain.LimitDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/29/2021
 */
@Repository
public interface LimitDiscountRepository extends JpaRepository<LimitDiscount,Long> {

//    get discount rate
        @Query("select dp from LimitDiscount dp where dp.id = :idPod ")
        LimitDiscount discountRate(Long idPod);

        @Query("select ld from LimitDiscount ld where ld.discountCommission.id = ?1 order by ld.minOrder")
        List<LimitDiscount> getLimitByDiscount(long id);

        LimitDiscount findLimitDiscountByDiscountCommission(DiscountCommission discount);
}
