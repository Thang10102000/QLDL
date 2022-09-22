package com.neo.vas.service.impl;

import com.neo.vas.domain.AgencyOrderPayment;
import com.neo.vas.repository.AgencyOrderPaymentRepository;
import com.neo.vas.service.AgencyOrderPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AgencyOrderPaymentServiceImpl implements AgencyOrderPaymentService {
    @Autowired
    private AgencyOrderPaymentRepository agencyOrderPaymentRepository;
    @Override
    public List<AgencyOrderPayment> getAll() {
        return agencyOrderPaymentRepository.findAll();
    }

    @Override
    public AgencyOrderPayment getPaymentByOrderId(Long orderId) {
        return agencyOrderPaymentRepository.agencyOderPayment(orderId);
    }

}
