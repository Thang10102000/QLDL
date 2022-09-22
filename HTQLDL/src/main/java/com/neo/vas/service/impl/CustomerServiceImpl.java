package com.neo.vas.service.impl;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Customer;
import com.neo.vas.repository.AgencyAreaRepository;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.CustomerRepository;
import com.neo.vas.repository.SchoolRepository;
import com.neo.vas.service.CustomerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository repo;
    @Autowired
    private AgencyAreaRepository areaRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Override
    public Page<Customer> searchCustomer(String name, String area, String school, String agencyCode, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page,pageSize);
        if(name != null || area != null || school != null)
        {
            return repo.searchCustomer(name,area,school,agencyCode,pageable);
        }
        return repo.findAll(pageable);
    }

    @Override
    public boolean createCustomer(JSONObject data, Principal principal) {
        Customer customer = new Customer();
        if (!data.getString("nameCus").isEmpty())
        {
            customer.setName(data.getString("nameCus").trim());
        }
        if (!data.getString("domain").isEmpty())
        {
            customer.setDomain(data.getString("domain").trim());
        }
        if (!data.getString("school").isEmpty())
        {
            customer.setSchoolId(schoolRepository.getOne(data.getLong("school")));
        }
        if (!data.getString("area").isEmpty())
        {
            customer.setAreaCId(areaRepository.findAgencyAreaByAreaId(Long.parseLong(data.getString("area").trim())));
        }

        if (!data.getString("address").isEmpty())
        {
            customer.setAddress(data.getString("address").trim());
        }
        if (!data.getString("province").isEmpty())
        {
            customer.setProvince(data.getString("province").trim());
        }
        if (!data.getString("district").isEmpty())
        {
            customer.setDistrict(data.getString("district").trim());
        }
        if (!data.getString("wards").isEmpty())
        {
            customer.setWards(data.getString("wards").trim());
        }
        if (data.has("agencyCode") && !data.getString("agencyCode").isEmpty())
        {
            customer.setAgencyCode(data.getString("agencyCode"));
        }
//        if (data.has("amCode") && !data.getString("amCode").isEmpty())
//        {
//            customer.setAgencyCode(data.getString("amCode"));
//        }
        if (!data.getString("email").isEmpty()){
            customer.setEmail(data.getString("email").trim());
        }
        if (!data.getString("phone").isEmpty()){
            customer.setPhone(data.getString("phone").trim());
        }

        customer.setCreatedBy(principal.getName());
        customer.setCreatedDate(new Date());
        return repo.saveAndFlush(customer) != null;
    }

    @Override
    public boolean saveCustomer(JSONObject data, Principal principal) {
        Customer customer;
        try {
            if (!data.getString("id").isEmpty()) {
                try {
                    customer = repo.findCustomerById(Long.parseLong(data.getString("id")));
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("name").isEmpty())
            {
                customer.setName(data.getString("name").trim());
            }
            if (!data.getString("domain").isEmpty())
            {
                customer.setDomain(data.getString("domain").trim());
            }
            if (!data.getString("schoolId").isEmpty())
            {
                customer.setSchoolId(schoolRepository.getOne(data.getLong("schoolId")));
            }
            if (!data.getString("address").isEmpty())
            {
                customer.setAddress(data.getString("address").trim());
            }
            if (!data.getString("province").isEmpty())
            {
                customer.setProvince(data.getString("province").trim());
            }
            if (!data.getString("district").isEmpty())
            {
                customer.setDistrict(data.getString("district").trim());
            }
            if (!data.getString("wards").isEmpty())
            {
                customer.setWards(data.getString("wards").trim());
            }
            if (!data.getString("areaCId").isEmpty())
            {
                customer.setAreaCId(areaRepository.getOne(data.getLong("areaCId")));
            }
            if (!data.getString("agencyCode").isEmpty())
            {
                customer.setAgencyCode(data.getString("agencyCode"));
            }
            if (!data.getString("email").isEmpty()){
                customer.setEmail(data.getString("email").trim());
            }
            if (!data.getString("phone").isEmpty()){
                customer.setPhone(data.getString("phone").trim());
            }
            customer.setUpdateBy(principal.getName());
            customer.setUpdateDate(new Date());

            return repo.saveAndFlush(customer) !=null;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public List<Customer> getCustomerByAreaId(Long areaId) {
        return repo.getCustomerByAreaId(areaId);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return repo.findAll();
    }

    @Override
    public Customer getCusById(Long id) {
        return repo.findCustomerById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.repo.deleteById(id);
    }

    @Override
    public Agency getAgencyByCode(String agencyCode) {
        try {
            return agencyRepository.findAgencyByAgencyCode(agencyCode);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
