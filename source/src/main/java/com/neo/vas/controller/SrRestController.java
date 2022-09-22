package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.ServiceRequest;
import com.neo.vas.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@RestController
public class SrRestController {
    @Autowired
    private ServiceRequestService srService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AgencyService agencyService;

    @GetMapping("/service-request/search")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Xem')")
    public Map<JSONObject, Integer> doSearchAsr(@RequestParam String packages, @RequestParam String sr_id,
                                                @RequestParam String customer, @RequestParam String status, @RequestParam String policy,
                                                @RequestParam String createdDateFrom, @RequestParam String createdDateTo,
                                                @RequestParam String approvalDateFrom, @RequestParam String approvalDateTo,@RequestParam String agencyArea,
                                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }
        HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
        try {
            Date createFrom = null;
            Date createTo = null;
            Date approvedFrom = null;
            Date approvedTo = null;
            if (!createdDateFrom.isEmpty() && createdDateFrom != null)
                createFrom = new SimpleDateFormat("dd/MM/yyyy").parse(createdDateFrom);
            if (!createdDateTo.isEmpty() && createdDateTo != null)
                createTo = new SimpleDateFormat("dd/MM/yyyy").parse(createdDateTo);
            if (!approvalDateFrom.isEmpty() && approvalDateFrom != null)
                approvedFrom = new SimpleDateFormat("dd/MM/yyyy").parse(approvalDateFrom);
            if (!approvalDateTo.isEmpty() && approvalDateTo != null)
                approvedTo = new SimpleDateFormat("dd/MM/yyyy").parse(approvalDateTo);
            if (!Pattern.matches("\\d+", packages))
                packages = "";
            if (!Pattern.matches("\\d+", customer))
                customer = "";
            if (!Pattern.matches("\\d+", agencyArea))
                agencyArea = "";
            Page<ServiceRequest> pages = srService.searchAsrs(packages, customer, sr_id, status, policy, createFrom,
                    createTo, approvedFrom, approvedTo, agencyArea, realPage, size);
            for (ServiceRequest asrs : pages) {
                JSONObject adata = asrs.createJson();
                data.put(adata, pages.getTotalPages());
            }
            insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.SEARCH,
                    principal.getName() + " search service request");
            return data;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
