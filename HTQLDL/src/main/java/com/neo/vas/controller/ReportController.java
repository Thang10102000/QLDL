package com.neo.vas.controller;
import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyArea;

import com.neo.vas.domain.Users;
import com.neo.vas.dto.AgencyAreaReportDTO;
import com.neo.vas.dto.ReportRevenueDTO;
import com.neo.vas.dto.ServiceRequestReportDTO;
import com.neo.vas.export.ExportRevenueAgency;
import com.neo.vas.export.ExportServiceRequestReport;
import com.neo.vas.dto.CommissionReportDTO;
import com.neo.vas.dto.DetailReportDTO;
import com.neo.vas.export.ExportAgencyAreaReport;
import com.neo.vas.export.ExportCommissionReport;
import com.neo.vas.export.ExportDetailReport;
import com.neo.vas.repository.ServiceRepository;
import com.neo.vas.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/report/")
public class ReportController {
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private BrandGroupService brandGroupService;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private BrandService brandService;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private ReportService reportService;
	@Autowired
	private ReportRevenueService reportRevenueService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ServiceRequestService serviceRequestService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private ServiceRepository serviceRepository;

	@GetMapping("product-revenue")
	public ModelAndView getProductRevenue(Model model) {
		model.addAttribute("listAgency", agencyService.getAllAgency());
		model.addAttribute("report", reportService.getReport());
		model.addAttribute("services", serviceRepository.findAll());
		return new ModelAndView("Report/report-product");
	}

	@PostMapping("product-revenue")
	public ModelAndView setProductRevenue(Model model, Principal principal) throws SQLException {
		insertLogService.insertLog(principal.getName(),"/vasonline/report/product-revenue", ConstantLog.SEARCH,
				principal.getName()+" search report product revenue");
		model.addAttribute("listAgency", agencyService.getAllAgency());
		return new ModelAndView("Report/report-product");
	}

	@GetMapping("agency-revenue")
	@PreAuthorize("hasAnyAuthority('Báo cáo doanh thu đại lý:Xem')")
	public ModelAndView getAgencyRevenue(Model model, Principal principal) {
		model.addAttribute("service",serviceInterface.getAllService());
		model.addAttribute("listBrandGroup", brandGroupService.getAllBrandGroup());
		model.addAttribute("listBrand", brandService.getAllBrand());
		model.addAttribute("services", serviceRepository.findAll());
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if (levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if (levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		return new ModelAndView("Report/report-agency");
	}

	@PostMapping("agency-revenue")
	public ModelAndView setAgencyRevenue(Model model, Principal principal) throws SQLException {
		insertLogService.insertLog(principal.getName(),"/vasonline/report/agency-revenue", ConstantLog.SEARCH,
				principal.getName()+" search report agency revenue");
		model.addAttribute("listAgency", agencyService.getAllAgency());
		return new ModelAndView("Report/report-agency");
	}
	
	
	@GetMapping("order-report")
	public ModelAndView getOrderReport(Model model) {
		model.addAttribute("listAgency", agencyService.getAllAgency());
		model.addAttribute("services", serviceRepository.findAll());
		return new ModelAndView("Report/report-order");
	}

	@PostMapping("order-report")
	public ModelAndView setOrderReport(Model model, Principal principal) throws SQLException {
		insertLogService.insertLog(principal.getName(),"/vasonline/report/order-report", ConstantLog.SEARCH,
				principal.getName()+" search order report ");
		model.addAttribute("listAgency", agencyService.getAllAgency());
		return new ModelAndView("Report/report-order");
	}

	@GetMapping("details-report")
	@PreAuthorize("hasAnyAuthority('Báo cáo chi tiết:Xem')")
	public ModelAndView getDetailReport(Model model, Principal principal) {
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if (levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if (levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",(agency.getType() == 0) ? agency : null);
				model.addAttribute("listAmKam", (agency.getType() == 1) ? agency : null);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
//		model.addAttribute("listAgency", agencyService.getAllAgency());
//		model.addAttribute("listAmKam", agencyService.getAllAmKam());
		model.addAttribute("listBrandGroup", brandGroupService.getAllBrandGroup());
		model.addAttribute("listBrand", brandService.getAllBrand());
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("services", serviceRepository.findAll());
		ModelAndView modelAndView = new ModelAndView("Report/report-details");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping("commission-report")
	@PreAuthorize("hasAnyAuthority('Báo cáo chi phí hoa hồng:Xem')")
	public ModelAndView getCommissionReport(Model model, Principal principal) {
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if (levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if (levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
//		model.addAttribute("listAgency", agencyService.getAllAgency());
//		model.addAttribute("listAmKam", agencyService.getAllAmKam());
		model.addAttribute("listBrandGroup", brandGroupService.getAllBrandGroup());
		model.addAttribute("listBrand", brandService.getAllBrand());
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("services", serviceRepository.findAll());
		ModelAndView modelAndView = new ModelAndView("Report/report-commission");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping("agency-area-report")
	@PreAuthorize("hasAnyAuthority('Báo cáo tổng hợp kết quả CTKV:Xem')")
	public ModelAndView getAgencyAreaReport(Model model,Principal principal){
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if (levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if (levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		model.addAttribute("listBrandGroup", brandGroupService.getAllBrandGroup());
		model.addAttribute("listBrand", brandService.getAllBrand());
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("services", serviceRepository.findAll());
		ModelAndView modelAndView = new ModelAndView("Report/report-agency-area");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}
	@GetMapping("service-request-report")
	@PreAuthorize("hasAnyAuthority('Tổng hợp yêu cầu dịch vụ:Xem')")
	public ModelAndView getServiceRequestReport(Model model, Principal principal){
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if (levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if (levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		model.addAttribute("service", serviceInterface.getAllService());
		model.addAttribute("agency",agencyService.getAllAgency());
		model.addAttribute("customer",customerService.getAllCustomer());
		model.addAttribute("services", serviceRepository.findAll());

		model.addAttribute("serviceRequest",serviceRequestService.getAllServiceRequest());
		ModelAndView modelAndView = new ModelAndView("Report/service_request_report");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	//    export service request report
	@GetMapping("export-service-request-report")
	@PreAuthorize("hasAnyAuthority('Tổng hợp yêu cầu dịch vụ:Thực thi')")
	public void exportServiceReport(HttpServletResponse response, @RequestParam String serviceId, String brandGId, String brandId, String startDate, String endDate,
									String serviceRequestId, String agencyCode, String customerId, Principal principal ){
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/report/service-request-report", ConstantLog.SEARCH,
					principal.getName()+" export report service request");
			List<ServiceRequestReportDTO> dtoPage = reportService.searchServiceRequestReport(serviceId,brandGId,brandId,startDate,endDate,serviceRequestId,agencyCode,customerId);

			response.setContentType("application/octet-stream");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=service_request_report_" + currentDateTime + ".xlsx";
			response.setHeader(headerKey, headerValue);

			ExportServiceRequestReport excelExporter = new ExportServiceRequestReport(dtoPage);

			excelExporter.export(response);
		}catch (Exception e){
			System.err.println(e);
		}
	}
	@GetMapping("export-detail")
	@PreAuthorize("hasAnyAuthority('Báo cáo chi tiết:Thực thi')")
	public void exportDetail(HttpServletResponse response, @RequestParam String brandGroupName, @RequestParam String brandName,
							 @RequestParam String agencyCode, @RequestParam String areaName, @RequestParam String createdDate,
							 @RequestParam String endDate, @RequestParam String approvedDate,
							 @RequestParam String dateEnd, @RequestParam String reportMonth) throws SQLException, IOException {
		response.setContentType("application/octet-stream");
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = fm.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_detail" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		DetailReportDTO reportDTO = new DetailReportDTO();
		reportDTO.setBrandGroupName(brandGroupName);
		reportDTO.setBrandName(brandName);
		reportDTO.setAgencyCode(agencyCode);
		reportDTO.setAreaName(areaName);
		reportDTO.setCreatedDate(createdDate);
		reportDTO.setEndDate(endDate);
		reportDTO.setApprovedDate(approvedDate);
		reportDTO.setDateEnd(dateEnd);
		if (!reportMonth.isEmpty()) {
			reportDTO.setReportMonth(Integer.parseInt(reportMonth));
		} else{
			reportDTO.setReportMonth(0);
		}
		List<DetailReportDTO> listDetailReportDTO = reportService.searchServiceRequest(reportDTO);

		ExportDetailReport exportDetailReport = new ExportDetailReport(listDetailReportDTO);
		exportDetailReport.export(response);
	}

	@GetMapping("export-revenue")
	@PreAuthorize("hasAnyAuthority('Báo cáo doanh thu đại lý:Thực thi')")
	public void exportRevenue(HttpServletResponse response, @RequestParam String brandGroupId, @RequestParam String brandId,
							  @RequestParam String costAmKam, @RequestParam String areaId,
							  @RequestParam String amountUnactivated, @RequestParam String createdDate,
							  @RequestParam String approvedDate) throws SQLException, IOException {
		response.setContentType("application/octet-stream");
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = fm.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_revenue" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		ReportRevenueDTO reportDTO = new ReportRevenueDTO();
		reportDTO.setBrandGroupName(brandGroupId);
		reportDTO.setBrandName(brandId);
		reportDTO.setAgencyCode(costAmKam);
		reportDTO.setAreaId(areaId);
		reportDTO.setCreatedDate(createdDate);
//		reportDTO.setEndDate(endDate);
		reportDTO.setApprovedDate(approvedDate);
//		reportDTO.setDateEnd(dateEnd);
		if (!amountUnactivated.isEmpty()) {
			reportDTO.setAmountUnactivated(Integer.parseInt(amountUnactivated));
		} else{
			reportDTO.setAmountUnactivated(0);
		}
		List<ReportRevenueDTO> list = reportRevenueService.searchServiceRequest(reportDTO);

		ExportRevenueAgency export = new ExportRevenueAgency(list);
		export.export(response);
	}

	@GetMapping("export-commission")
	@PreAuthorize("hasAnyAuthority('Báo cáo chi phí hoa hồng:Thực thi')")
	public void exportCommission(HttpServletResponse response, @RequestParam String brandGroupName,String brandName,String agencyCode,
								 String areaName, String createdDate, String endDate, String approvedDate, String dateEnd, String reportMonth) throws SQLException, IOException {
		response.setContentType("application/octet-stream");
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = fm.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_commission" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		CommissionReportDTO reportDTO = new CommissionReportDTO();
		reportDTO.setBrandGrName(brandGroupName);
		reportDTO.setBrandName(brandName);
		reportDTO.setAgencyCode(agencyCode);
		reportDTO.setAreaName(areaName);
		reportDTO.setCreatedDate(createdDate);
		reportDTO.setEndDate(endDate);
		reportDTO.setApprovedDate(approvedDate);
		reportDTO.setApprovalEndDate(dateEnd);
		if (!reportMonth.isEmpty()) {
			reportDTO.setReportMonth(Integer.parseInt(reportMonth));
		} else{
			reportDTO.setReportMonth(0);
		}
		List<CommissionReportDTO> listCommissionReport = reportService.searchCommissionReport(reportDTO);
		ExportCommissionReport exportCommissionReportReport = new ExportCommissionReport(listCommissionReport);
		exportCommissionReportReport.export(response);
	}

	@GetMapping("export-agency-area-report")
	@PreAuthorize("hasAnyAuthority('Báo cáo tổng hợp kết quả CTKV:Thực thi')")
	public void exportAgencyAreaReport(HttpServletResponse response, @RequestParam String brandGroupName,String brandName, String areaName,
									   String createdDate, String endDate, String approvedDate, String approvedEndDate, String reportMonth) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = fm.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=report_agency_area" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		AgencyAreaReportDTO reportDTO = new AgencyAreaReportDTO();
		reportDTO.setBrandGrName(brandGroupName);
		reportDTO.setBrandName(brandName);
		reportDTO.setAreaName(areaName);
		reportDTO.setCreatedDate(createdDate);
		reportDTO.setEndDate(endDate);
		reportDTO.setApprovedDate(approvedDate);
		reportDTO.setApprovedEndDate(approvedEndDate);
		if (!reportMonth.isEmpty()) {
			reportDTO.setReportMonth(Integer.parseInt(reportMonth));
		} else{
			reportDTO.setReportMonth(0);
		}
		List<AgencyAreaReportDTO> agencyAreaReportDTOList = reportService.searchAgencyAreaReport(reportDTO);
		ExportAgencyAreaReport exportAgencyAreaReport = new ExportAgencyAreaReport(agencyAreaReportDTOList);
		exportAgencyAreaReport.export(response);
	}

}
