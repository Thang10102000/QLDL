package com.neo.vas.service;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Customer;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface CustomerService {
    Page<Customer> searchCustomer(String name, String area, String school,String agencyCode, int page, int pageSize);

    boolean createCustomer(JSONObject data, Principal principal);

    boolean saveCustomer(JSONObject data , Principal principal);

    List<Customer> getCustomerByAreaId(Long areaId);

    List<Customer> getAllCustomer();

    Customer getCusById(Long id);

    void deleteById(Long id);

    Agency getAgencyByCode(String agencyCode);

}
