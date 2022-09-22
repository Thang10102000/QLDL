package com.neo.vas.service.impl;

import com.neo.vas.domain.School;
import com.neo.vas.domain.Services;
import com.neo.vas.repository.SchoolRepository;
import com.neo.vas.service.SchoolService;
import com.neo.vas.service.specification.SchoolSpecification;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolRepository schoolRepository;

    Date date = new Date();

    @Override
    public List<School> getAll() {
        return schoolRepository.findAll();
    }

    @Override
    public boolean createSchool(JSONObject data, Principal principal) {
        School school = new School();
        try{
            if (!data.getString("schoolCode").trim().isEmpty()){
                school.setSchoolCode(data.getString("schoolCode").trim());
            }
            if (!data.getString("schoolType").trim().isEmpty()){
                school.setSchoolType(data.getString("schoolType").trim());
            }
            else {
                return false;
            }
            if (!data.getString("description").trim().isEmpty()) {
                school.setDescription(data.getString("description").trim());
            }else {
                school.setDescription("");
            }
            school.setCreatedBy(principal.getName());
            school.setCreatedDate(date);
            schoolRepository.saveAndFlush(school);
            return true;
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean editSchool(JSONObject data, Principal principal) {
        School school = new School();
        try {
            if (!data.getString("id").isEmpty()){
               try {
                   school = schoolRepository.findSchoolsById(Long.valueOf(data.getString("id")));
               } catch (Exception e){
                   System.out.println(e);
                   return false;
               }
            } else {
                return false;
            }
            if(!data.getString("schoolCode").trim().isEmpty()){
                school.setSchoolCode(data.getString("schoolCode").trim());
            }
            if(!data.getString("schoolType").trim().isEmpty()){
                school.setSchoolType(data.getString("schoolType").trim());
            }
            else {
                return false;
            }
            school.setDescription(data.getString("description").trim());
            school.setUpdateBy(principal.getName());
            school.setUpdateDate(date);
            schoolRepository.saveAndFlush(school);
            return true;
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
    }

    @Override
    public void deleteSchool(Long id) {
        this.schoolRepository.deleteById(id);
    }

    @Override
    public School getSchoolById(Long id) {
        Optional<School> optservices = schoolRepository.findById(id);
        School school = null;
        if (optservices.isPresent()) {
            school = optservices.get();
        } else {
            throw new RuntimeException("Không tìm thấy trường học " + id);
        }
        return school;
    }

    @Override
    public School findSchoolBySchoolCode(String schoolCode) {
        return schoolRepository.findSchoolBySchoolCode(schoolCode);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<School> searchSchool(School school, int page, int size) {
        Specification<School>  specification = Specification
                .where((school.getSchoolType().isEmpty()) ? null : SchoolSpecification.hasSchoolType(school.getSchoolType()))
                .and((school.getSchoolCode().isEmpty()) ? null : SchoolSpecification.hasSchoolCode(school.getSchoolCode()));
//                .and((school.getSchoolType().isEmpty()) ? null : SchoolSpecification.hasSchoolType(school.getSchoolType()))
//                .and((school.getDescription().isEmpty()) ? null : SchoolSpecification.hasDescription(school.getDescription()));
        return schoolRepository.findAll(specification, PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }
}
