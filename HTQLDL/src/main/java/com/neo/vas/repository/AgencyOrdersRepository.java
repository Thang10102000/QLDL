package com.neo.vas.repository;

import com.neo.vas.domain.AgencyOrderPayment;
import com.neo.vas.domain.AgencyOrderRequest;
import com.neo.vas.domain.AgencyOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 28/05/2021
 */
@Repository
public interface AgencyOrdersRepository extends JpaRepository<AgencyOrders, Long>, JpaSpecificationExecutor<AgencyOrders> {
    AgencyOrders findByOrderId(long id);


}
