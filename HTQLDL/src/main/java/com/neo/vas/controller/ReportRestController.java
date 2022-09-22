package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.SmsTemplate;
import com.neo.vas.dto.AgencyAreaReportDTO;
import com.neo.vas.dto.CommissionReportDTO;
import com.neo.vas.dto.DetailReportDTO;
import com.neo.vas.dto.ServiceRequestReportDTO;
import com.neo.vas.export.ExportServiceRequestReport;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.impl.ReportServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ReportRestController {
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private ReportServiceImpl reportService;

    @GetMapping(value = "/search-report-detail")
    public Map<JSONObject, Integer> searchReportDetail(@RequestParam String brandGroupName, @RequestParam String brandName,
                                                     @RequestParam String agencyCode, @RequestParam String areaName, @RequestParam String createdDate,
                                                     @RequestParam String endDate,@RequestParam String approvedDate,
                                                     @RequestParam String dateEnd, @RequestParam String reportMonth,
                                                     @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                     @RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal){
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 10;
        }
        HashMap<JSONObject, Integer> data = new HashMap<>();
        try {
            insertLogService.insertLog(principal.getName(),"/vasonline/report/details-report", ConstantLog.SEARCH,
                    principal.getName()+" search report detail");
            DetailReportDTO reportDTO = new DetailReportDTO();
            if (!brandGroupName.isEmpty()) {
                reportDTO.setBrandGroupName(brandGroupName);
            }
            if (!brandName.isEmpty()) {
                reportDTO.setBrandName(brandName);
            }
            if (!agencyCode.isEmpty()) {
                reportDTO.setAgencyCode(agencyCode);
            }
            if (!areaName.isEmpty()) {
                reportDTO.setAreaName(areaName);
            }
            if (!createdDate.isEmpty()) {
                reportDTO.setCreatedDate(createdDate);
                System.out.println(createdDate);
            }
            if (!endDate.isEmpty()) {
                reportDTO.setEndDate(endDate);
                System.out.println(endDate);
            }
            if (!approvedDate.isEmpty()) {
                reportDTO.setApprovedDate(approvedDate);
                System.out.println(approvedDate);
            }
            if (!dateEnd.isEmpty()) {
                reportDTO.setDateEnd(dateEnd);
                System.out.println(dateEnd);
            }
            if (!reportMonth.isEmpty()) {
                reportDTO.setReportMonth(Integer.parseInt(reportMonth));
            } else{
              reportDTO.setReportMonth(0);
            }
            List<DetailReportDTO> list = reportService.searchServiceRequest(reportDTO);
            Pageable pageable = PageRequest.of(realPage, size, Sort.by("approvedDate"));
			Page<DetailReportDTO> pages =  new PageImpl<>(list, pageable, list.size());
            for (DetailReportDTO ddt : pages) {
                JSONObject smsObj = ddt.createJson();
                data.put(smsObj, pages.getTotalPages());
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @GetMapping(value = "/search-report-commission")
    public Map<JSONObject, Integer> searchReportCommission(@RequestParam String brandGroupName, @RequestParam String brandName,
                                                           @RequestParam String agencyCode, @RequestParam String areaName, @RequestParam String createdDate,
                                                           @RequestParam String endDate,@RequestParam String approvedDate,
                                                           @RequestParam String dateEnd, @RequestParam String reportMonth,
                                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 10;
        }
        HashMap<JSONObject, Integer> data = new HashMap<>();
        try {
            insertLogService.insertLog(principal.getName(),"/vasonline/report/commission-report", ConstantLog.SEARCH,
                    principal.getName()+" search report commission");
            CommissionReportDTO reportDTO = new CommissionReportDTO();
            if(!brandGroupName.isEmpty()){
                reportDTO.setBrandGrName(brandGroupName);
            }
            if (!brandName.isEmpty()){
                reportDTO.setBrandName(brandName);
            }
            if(!agencyCode.isEmpty()){
                reportDTO.setAgencyCode(agencyCode);
            }
            if(!areaName.isEmpty()){
                reportDTO.setAreaName(areaName);
            }
            if (!createdDate.isEmpty()){
                reportDTO.setCreatedDate(createdDate);
            }
            if (!endDate.isEmpty()){
                reportDTO.setEndDate(endDate);
            }
            if (!approvedDate.isEmpty()) {
                reportDTO.setApprovedDate(approvedDate);
            }
            if (!dateEnd.isEmpty()) {
                reportDTO.setApprovalEndDate(dateEnd);
            }
            if (!reportMonth.isEmpty()) {
                reportDTO.setReportMonth(Integer.parseInt(reportMonth));
            } else{
                reportDTO.setReportMonth(0);
            }
            List<CommissionReportDTO> list = reportService.searchCommissionReport(reportDTO);
            Pageable pageable = PageRequest.of(realPage, size);
            Page<CommissionReportDTO> pages =  new PageImpl<>(list, pageable, list.size());
            for (CommissionReportDTO dto : pages){
                JSONObject smsObj = dto.createJson();
                data.put(smsObj, pages.getTotalPages());
            }
            return data;
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

    @GetMapping(value = "/search-report-agency-area")
    public Map<JSONObject, Integer> searchReportAgencyArea(@RequestParam String brandGroupName, @RequestParam String brandName,
                                                           @RequestParam String areaName, @RequestParam String createdDate,
                                                           @RequestParam String endDate, @RequestParam String approvedDate,
                                                           @RequestParam String approvedEndDate ,@RequestParam String reportMonth,
                                                           @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                           @RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal) {

        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 10;
        }
        HashMap<JSONObject, Integer> data = new HashMap<>();
        try {
            insertLogService.insertLog(principal.getName(),"/vasonline/report/agency-area-report", ConstantLog.SEARCH,
                    principal.getName()+" search report agency area");
            AgencyAreaReportDTO reportDTO = new AgencyAreaReportDTO();
            if (!brandGroupName.isEmpty()){
                reportDTO.setBrandGrName(brandGroupName);
            }
            if (!brandName.isEmpty()){
                reportDTO.setBrandName(brandName);
            }
            if (!areaName.isEmpty()){
                reportDTO.setAreaName(areaName);
            }
            if (!createdDate.isEmpty()){
                reportDTO.setCreatedDate(createdDate);
            }
            if (!endDate.isEmpty()){
                reportDTO.setEndDate(endDate);
            }
            if (!approvedDate.isEmpty()){
                reportDTO.setApprovedDate(approvedDate);
            }
            if (!approvedEndDate.isEmpty()){
                reportDTO.setApprovedEndDate(approvedEndDate);
            }
            if (!reportMonth.isEmpty()) {
                reportDTO.setReportMonth(Integer.parseInt(reportMonth));
            } else {
                reportDTO.setReportMonth(0);
            }
            List<AgencyAreaReportDTO> list = reportService.searchAgencyAreaReport(reportDTO);
            Pageable pageable = PageRequest.of(realPage, size);
            Page<AgencyAreaReportDTO> pages =  new PageImpl<>(list, pageable, list.size());
            for (AgencyAreaReportDTO dto : pages){
                JSONObject smsObj = dto.createJson();
                data.put(smsObj, pages.getTotalPages());
            }
            return data;
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

    @GetMapping(value = "/search-service-request-report")
    public Map<JSONObject, Integer> searchServiceReport(@RequestParam String serviceId,String brandGId, String brandId, String startDate, String endDate,
                                                        String serviceRequestId, String agencyCode, String customerId,
                                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                        @RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 10;
        }
        HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
        try {
            insertLogService.insertLog(principal.getName(),"/vasonline/report/service-request-report", ConstantLog.SEARCH,
                            principal.getName()+" search report service request");
            List<ServiceRequestReportDTO> dtoList = reportService.searchServiceRequestReport(serviceId,brandGId,brandId,startDate,endDate,serviceRequestId,agencyCode,customerId);
            Pageable pageable = PageRequest.of(realPage, size);
            Page<ServiceRequestReportDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());
            for (ServiceRequestReportDTO dto : dtoPage){
                JSONObject smsObj = dto.createJson();
                data.put(smsObj, dtoPage.getTotalPages());
            }
            return data;
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }
}
