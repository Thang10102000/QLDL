package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.ApiTransactionLog;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.TransactionApiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class ApiTransactionLogRestController {

    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private TransactionApiService transactionApiService;

    @GetMapping("/search-atl")
    public Map<JSONObject, Integer> searchApiTransactionLog(@RequestParam String startDate, String endDate,
                                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
        //insertLogService.insertLog(principal.getName(),"/vasonline/api-transaction-log", ConstantLog.SEARCH, principal.getName()+" search api transaction log");

        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }
        HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
        try{
        if(startDate != null)
        {
            startDate = startDate.trim();
        }
        if(endDate != null)
        {
            endDate = endDate.trim();
        }
        Page<ApiTransactionLog> transactionLogs = transactionApiService.
            searchApiTransactionLog(startDate, endDate, realPage, size);
        for (ApiTransactionLog atl : transactionLogs){
            JSONObject js = atl.createJson();
            data.put(js, transactionLogs.getTotalPages());
        }
        System.out.println(data);
        return data;
    }catch (Exception e){
        System.err.println("Lỗi của search: " + e);
        return null;
    }

    }
}
