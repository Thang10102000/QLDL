package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Customer;
import com.neo.vas.repository.DistrictRepository;
import com.neo.vas.repository.ProvinceRepository;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.CustomerService;
import com.neo.vas.service.WardService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class CustomerRestController {
    @Autowired
    CustomerService customerService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private WardService wardService;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    ProvinceRepository provinceRepository;

    @GetMapping("cus-search")
//    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Xem')")
    public Map<JSONObject, Integer> searchAgency(@RequestParam String name, String area,
                                                 String school,String agencyCodeSearch, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "5") int size , Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }
        Map<JSONObject, Integer> data = new LinkedHashMap<>();
        try {
            if(!name.isEmpty()){
                name = name.toLowerCase().trim();
            }
            Page<Customer> pageAgency = customerService.searchCustomer(name,area,school,agencyCodeSearch, realPage, size);
            for (Customer cus : pageAgency) {
                JSONObject js = createJson(cus);
                data.put(js, pageAgency.getTotalPages());
            }
            insertLogService.insertLog(principal.getName(),"/vasonline/customer", ConstantLog.SEARCH, principal.getName()+" search customer");
            return data;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    public JSONObject createJson(Customer cus) {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Agency agency = customerService.getAgencyByCode(cus.getAgencyCode());
        jsonObject.put("id",cus.getId());
        jsonObject.put("name",cus.getName());
        jsonObject.put("createdDate", (cus.getCreatedDate() == null) ? "" : fm.format(cus.getCreatedDate()));
        jsonObject.put("updateDate", (cus.getUpdateDate() == null) ? "" : fm.format(cus.getUpdateDate()));
        jsonObject.put("area",cus.getAreaCId().getAreaName());
        jsonObject.put("school",cus.getSchoolId().getSchoolType());
        jsonObject.put("address",cus.getAddress() + " / "+ wardService.findWardById(Long.parseLong(cus.getWards())).getWardName() +" / "+ districtRepository.findDistrictById(Long.parseLong(cus.getDistrict())).getDistrictName() +" / " +provinceRepository.findProvinceById(Long.parseLong(cus.getProvince())).getProvince_name());
        jsonObject.put("agencyCode",agency.getAgencyName());
        return jsonObject;
    }
}
