package com.neo.vas.service;

import com.neo.vas.domain.School;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface SchoolService {
    List<School> getAll();
    boolean createSchool(JSONObject data, Principal principal);
    boolean editSchool(JSONObject data, Principal principal);
    void deleteSchool(Long id);
    School getSchoolById(Long id);
    School findSchoolBySchoolCode(String schoolCode);
    Page<School> searchSchool(School school, int page, int size);
}
