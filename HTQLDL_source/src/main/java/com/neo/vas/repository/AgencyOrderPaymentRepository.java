package com.neo.vas.repository;

import com.neo.vas.domain.AgencyOrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 01/06/2021
 */
@Repository
public interface AgencyOrderPaymentRepository extends JpaRepository<AgencyOrderPayment,Long>, JpaSpecificationExecutor<AgencyOrderPayment> {
    @Query("select ap from AgencyOrderPayment ap where ap.agencyOrdersAOP.orderId = ?1 order by ap.createdDate asc ")
    List<AgencyOrderPayment> listAgencyOderPayment(Long orderId);
    @Query("select ap from AgencyOrderPayment ap where ap.agencyOrdersAOP.orderId = ?1 ")
    AgencyOrderPayment agencyOderPayment(Long orderId);
}
