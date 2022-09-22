package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.AgencyDiscountCommission;
import com.neo.vas.domain.Deposits;
import com.neo.vas.service.DepositeService;
import com.neo.vas.service.InsertLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 8/14/2021
 */
@RestController
public class DepositRestController {
    @Autowired
    private DepositeService depositeService;
    @Autowired
    private InsertLogService insertLogService;

    @GetMapping("/search-deposit-agency")
    @PreAuthorize("hasAnyAuthority('Quản lý bảo lãnh - đặt cọc:Xem')")
    public Map<JSONObject, Integer> searchAgDi(@RequestParam String agencyName, String startDate, String endDate, String depositeAmount, String status,
                                               @RequestParam(name = "page", required = false, defaultValue = "1") int page,
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
            insertLogService.insertLog(principal.getName(), "/vasonline/deposite/index", ConstantLog.SEARCH,
				principal.getName() + " search deposit");
            Page<Deposits> adPage = depositeService.getAll(agencyName, depositeAmount,startDate,endDate,status, realPage, size);
            for (Deposits ad : adPage) {
                JSONObject js = ad.createJson();
                data.put(js, adPage.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
