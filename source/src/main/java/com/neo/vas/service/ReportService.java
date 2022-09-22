package com.neo.vas.service;

import java.sql.SQLException;
import java.util.List;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyOrders;
import com.neo.vas.dto.AgencyAreaReportDTO;
import com.neo.vas.dto.CommissionReportDTO;
import com.neo.vas.dto.DetailReportDTO;
import com.neo.vas.dto.ServiceRequestReportDTO;
import org.springframework.data.domain.Page;

public interface ReportService {
	List<Agency> getReport();

	List<DetailReportDTO> searchServiceRequest(DetailReportDTO dto) throws SQLException;

	List<CommissionReportDTO> searchCommissionReport(CommissionReportDTO dto) throws SQLException;

	List<ServiceRequestReportDTO> searchServiceRequestReport(String serviceId, String brandGId, String brandId, String startDate, String endDate, String serviceRequestId, String agencyCode, String customerId);

	List<AgencyAreaReportDTO> searchAgencyAreaReport(AgencyAreaReportDTO dto);
}
