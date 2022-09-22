package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.LogAPI;
import com.neo.vas.service.ApiLogService;
import com.neo.vas.service.InsertLogService;
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
public class ApiLogRestController {
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private ApiLogService apiLogService;

    @GetMapping("/search-api-log")
    public Map<JSONObject, Integer> searchApiLog(@RequestParam String alId, String module,
                                                 String startDate, String endDate, String creator, String referenceId,
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
            insertLogService.insertLog(principal.getName(),"/vasonline/api-log", ConstantLog.SEARCH, principal.getName()+" search api log");
            Long apiLogId = null;
            Long moduleId = null;
            Long rfId = null;
            if(!alId.isEmpty()) {
                alId = alId.trim();
                apiLogId = Long.parseLong(alId);
            }
            if(!module.isEmpty()) {
                moduleId = Long.parseLong(module);
            }
            if(!startDate.isEmpty())
            {

                startDate = startDate.trim();
            }
            if(!endDate.isEmpty()) {
                endDate = endDate.trim();
            }
            if(!creator.isEmpty()) {
                creator = creator.toLowerCase().trim();
            }
            if(!referenceId.trim().isEmpty()) {
                rfId = Long.parseLong(referenceId.trim());
            }
            Page<LogAPI> logAPIPage = apiLogService.searchApiLog(apiLogId, moduleId, startDate, endDate, creator, rfId, realPage, size);
            for (LogAPI al : logAPIPage){
                JSONObject js = al.createJson();
                data.put(js, logAPIPage.getTotalPages());
            }
//            System.out.println(data);
            return data;
        }catch (Exception e){
            System.err.println("Lỗi của search: " + e);
            return null;
        }
    }
}
