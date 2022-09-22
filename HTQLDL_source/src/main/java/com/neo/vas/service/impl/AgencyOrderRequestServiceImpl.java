package com.neo.vas.service.impl;

import com.neo.vas.domain.ServiceRequest;
import com.neo.vas.repository.AgencyOrderRequestRepository;
import com.neo.vas.repository.ServiceRequestRepository;
import com.neo.vas.service.AgencyOrderRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyOrderRequestServiceImpl implements AgencyOrderRequestService {
    @Autowired
    private AgencyOrderRequestRepository orderRequestRepository;

    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    @Override
    public List<ServiceRequest> getServiceRequestByAoId(Long id) {
        List<Long> listIdServiceRequestId = orderRequestRepository.getListIdServiceRequest(id);
        System.out.println(serviceRequestRepository.getServiceRequestByAorId(listIdServiceRequestId));
        return serviceRequestRepository.getServiceRequestByAorId(listIdServiceRequestId);
    }
}
