package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.dto.DetailReportDTO;
import com.neo.vas.dto.ReportRevenueDTO;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.ReportRevenueService;
import com.neo.vas.service.impl.ReportServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ReportRevenueController {
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private ReportRevenueService reportService;

    @GetMapping(value = "/search-report-revenue")
    public Map<JSONObject, Integer> searchReportDetail(@RequestParam String brandGroupId, @RequestParam String brandId,
                                                       @RequestParam String costAmKam, @RequestParam String areaId,
                                                       @RequestParam String amountUnactivated, @RequestParam String createdDate,
                                                       @RequestParam String endDate ,@RequestParam String approvedDate,
                                                       @RequestParam String fromDate,
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
//            insertLogService.insertLog(principal.getName(),"/vasonline/report/revenue-report", ConstantLog.SEARCH,
//                    principal.getName()+" search report detail");
            ReportRevenueDTO reportDTO = new ReportRevenueDTO();
            if (!brandGroupId.isEmpty()) {
                reportDTO.setBrandGroupId(brandGroupId);
            }
            if (!brandId.isEmpty()) {
                reportDTO.setBrandId(brandId);
            }
            if (!costAmKam.isEmpty()) {
                reportDTO.setCostAmKam(costAmKam);
            }
            if (!areaId.isEmpty()) {
                reportDTO.setAreaId(areaId);
            }
            if (!amountUnactivated.isEmpty()) {
                reportDTO.setAmountUnactivated(Integer.parseInt(amountUnactivated));
            }else {
                reportDTO.setAmountUnactivated(0);
            }
            if (!createdDate.isEmpty()) {
//                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(createdDate);
                reportDTO.setCreatedDate(createdDate);
            }
            if (!endDate.isEmpty()) {
//                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(approvedDate);
                reportDTO.setEndDate(endDate);
            }
            if (!approvedDate.isEmpty()) {
//                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(approvedDate);
                reportDTO.setApprovedDate(approvedDate);
            }
            if (!fromDate.isEmpty()) {
//                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(approvedDate);
                reportDTO.setFromDate(fromDate);
            }
            Page<ReportRevenueDTO> pages = reportService.searchServiceRequest(reportDTO, realPage, size);
            for (ReportRevenueDTO ddt : pages) {
                JSONObject smsObj = ddt.createJson();
                data.put(smsObj, pages.getTotalPages());
            }
            return data;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
