package com.neo.vas.repository;

import com.neo.vas.domain.AgencyOrderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgencyOrderRequestRepository extends JpaRepository<AgencyOrderRequest, Long> {
    @Query("select aor.serviceRequest.srId from AgencyOrderRequest aor where aor.agencyOrders.orderId = ?1")
    List<Long> getListIdServiceRequest(Long id);
    @Query("select aor from AgencyOrderRequest aor where aor.serviceRequest.srId = ?1")
    List<Long> getListIdAgencyOrderRequest(Long id);
    @Query("select aor from AgencyOrderRequest aor where aor.id = ?1 and aor.agencyOrders.orderId = ?2")
    AgencyOrderRequest getById(Long id, Long idOrder);

    @Query("select ar from AgencyOrderRequest ar where ar.agencyOrders.orderId = ?1 ")
    List<AgencyOrderRequest> getARByOrderId(long orderId);
}
