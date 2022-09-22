package com.neo.vas.service;

import com.neo.vas.domain.ServiceRequest;

import java.util.List;

public interface AgencyOrderRequestService {
    List<ServiceRequest> getServiceRequestByAoId(Long id);
}
