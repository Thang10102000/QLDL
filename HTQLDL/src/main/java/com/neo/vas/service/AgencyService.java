package com.neo.vas.service;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyArea;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 21/05/2021
 */
public interface AgencyService {
    List<Agency> getAll();
    List<Agency> getAllAgency();
    List<Agency> getAllAmKam();
    Page<Agency> searchAgency(String agencyName, String areaId, String status, String type, int page, int size);
    Agency getAgencyById(Long id);
    Agency getOne(Long id);
    boolean createAgency(JSONObject data, Principal principal);
    boolean updateAgency(JSONObject data, Principal principal);
    void deleteAgency(Long id);
    List<Agency> getListAgencyByArea(Long areaId);
    List<Agency> getLIstAgencyByType(int type);
    List<Agency> getAgencyByArea(Long areaId);

    // status and type =0
    List<Agency> getListAgencyById(Long id);
    List<Agency> getAgencyByArea(List<AgencyArea> idArea);
    Agency getAgencyByCode(String agencyCode);
}
