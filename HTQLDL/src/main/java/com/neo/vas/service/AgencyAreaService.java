package com.neo.vas.service;

import com.neo.vas.domain.AgencyArea;
import com.neo.vas.domain.Brand;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 21/05/2021
 */
public interface AgencyAreaService {
    List<AgencyArea> getAllAgencyArea();
    AgencyArea getAgencyAreaById(Long id);
    AgencyArea getAgencyAreaByCode(String areaCode);
    void deleteAgencyArea(Long id);

    Page<AgencyArea> searchAgencyArea(String areaCode,String areaName, String taxCode, String description, int page, int pageSize);

    boolean createAgencyArea(JSONObject data, Principal principal);

    boolean saveAgencyArea(JSONObject data , Principal principal);

}
