package com.neo.vas.service;

import com.neo.vas.domain.AgencyOrderPayment;

import java.util.List;

public interface AgencyOrderPaymentService {
    List<AgencyOrderPayment> getAll();
    AgencyOrderPayment getPaymentByOrderId(Long orderId);
}
