package com.neo.vas.service;

import java.security.Principal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.Services;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 13/05/2021
 */
public interface ServiceInterface {
    List<Services> getAllService();

    Page<Services> searchService(Services services, int page, int pageSize);

    boolean createService(JSONObject data, Principal principal);

    boolean editService(JSONObject data, Principal principal);

    Services getServicesById(Long id);

    Services getServiceByBG(Long id);

    void deleteServices(Long id);

}
