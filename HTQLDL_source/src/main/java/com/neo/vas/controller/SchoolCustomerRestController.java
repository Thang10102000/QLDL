package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.School;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SchoolService;
import com.neo.vas.service.impl.SchoolServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class SchoolCustomerRestController {
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private InsertLogService insertLogService;

    @GetMapping(value = "/school-search")
    public Map<JSONObject, Integer> searchSchool(@RequestParam String schoolCode, String schoolType, @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }
        Map<JSONObject, Integer> data = new LinkedHashMap<>();
        try {
            insertLogService.insertLog(principal.getName(), "/vasonline/school", ConstantLog.SEARCH,
                    principal.getName() + " search school");
            School school = new School();
            school.setSchoolType(schoolType.trim());
            school.setSchoolCode(schoolCode.trim());
            Page<School> pageSchool = schoolService.searchSchool(school, realPage, size);
            for (School s : pageSchool) {
                JSONObject dt = s.createJson();
                data.put(dt, pageSchool.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
