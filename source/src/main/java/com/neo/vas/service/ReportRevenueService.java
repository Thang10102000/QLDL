package com.neo.vas.service;

import com.neo.vas.domain.Agency;
import com.neo.vas.dto.ReportRevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ReportRevenueService {
    List<Agency> getReport();
    Page<ReportRevenueDTO> searchServiceRequest(ReportRevenueDTO dto , int page, int size );
    List<ReportRevenueDTO> searchServiceRequest(ReportRevenueDTO dto);
}
