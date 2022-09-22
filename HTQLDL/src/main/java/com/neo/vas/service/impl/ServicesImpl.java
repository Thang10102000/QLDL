package com.neo.vas.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.Services;
import com.neo.vas.repository.ServiceRepository;
import com.neo.vas.service.ServiceInterface;
import com.neo.vas.service.specification.SearchSBG;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 13/05/2021
 */
@Service
public class ServicesImpl implements ServiceInterface {

    @Autowired
    private ServiceRepository serviceRepository;

    Date date = new Date();

    @Override
    public List<Services> getAllService() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Services> searchService(Services services, int page, int pageSize) {
        Specification<Services> conditions = Specification
                .where((services.getServiceId().isEmpty()) ? SearchSBG.hasServiceId("")
                        : SearchSBG.hasServiceId(services.getServiceId()))
                .and((services.getServiceName().isEmpty()) ? null : SearchSBG.hasServiceName(services.getServiceName()))
                .and((services.getShortCode().isEmpty()) ? null : SearchSBG.hasShortCode(services.getShortCode()))
                .and((services.getStatus() != 100) ? SearchSBG.hasStatus(services.getStatus()) : null);

        Page<Services> pageServices = serviceRepository.findAll(conditions, PageRequest.of(page,pageSize, Sort.by("createdDate").descending()));
        System.err.println(pageServices);
        return pageServices;
    }


    @Override
    public boolean createService(JSONObject data, Principal principal) {
        Services services = new Services();
        try {
            if (!data.getString("serviceId").trim().isEmpty()) {
                services.setServiceId(data.getString("serviceId").trim());
            }
            if (!data.getString("serviceName").trim().isEmpty()) {
                services.setServiceName(data.getString("serviceName").trim());
            }
            if (!data.getString("shortCode").isEmpty()) {
                services.setShortCode(data.getString("shortCode").trim());
            } else {
                return false;
            }
            services.setStatus(0);
            services.setDescription(data.getString("description").trim());
            services.setCreatedBy(principal.getName());
            services.setCreatedDate(date);
            return serviceRepository.saveAndFlush(services) != null;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean editService(JSONObject data, Principal principal) {
        Services services = new Services();
        try {
            if (!data.getString("id").isEmpty()) {
                try {
                    services = serviceRepository.findServicesById(data.getLong("id"));
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("serviceId").trim().isEmpty()) {
                services.setServiceId(data.getString("serviceId").trim());
            }
            if (!data.getString("serviceName").trim().isEmpty()) {
                services.setServiceName(data.getString("serviceName").trim());
            }
            if (!data.getString("shortCode").isEmpty()) {
                services.setShortCode(data.getString("shortCode").trim());

            } else {
                return false;
            }
            if (!data.getString("status").isEmpty()) {
                services.setStatus(Integer.parseInt(data.getString("status")));
            }
            services.setDescription(data.getString("description").trim());
            services.setUpdateBy(principal.getName());
            services.setUpdateDate(date);
            return serviceRepository.saveAndFlush(services) != null;
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Services getServicesById(Long id) {
        Optional<Services> optservices = serviceRepository.findById(id);
        Services services = null;
        if (optservices.isPresent()) {
            services = optservices.get();
        } else {
            throw new RuntimeException("Không tìm thấy dịch vụ " + id);
        }
        return services;
    }

    @Override
    public Services getServiceByBG(Long id) {
        return serviceRepository.findServicesByBG(id);
    }

    @Override
    public void deleteServices(Long id) {
        this.serviceRepository.deleteById(id);
    }
}
