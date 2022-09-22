package com.neo.vas.service.impl;

import com.neo.vas.domain.AgencyArea;
import com.neo.vas.repository.AgencyAreaRepository;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.util.VNCharacterUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AgencyAreaServiceImpl implements AgencyAreaService {
    @Autowired
    private AgencyAreaRepository agencyAreaRepository;

    @Override
    public List<AgencyArea> getAllAgencyArea() {
        List<AgencyArea> listArea = agencyAreaRepository.findAll();
        Comparator<AgencyArea> areaComparator = Comparator.comparing(AgencyArea::getAreaName);
        listArea.sort(areaComparator);
        return listArea;
    }

    @Override
    public AgencyArea getAgencyAreaById(Long id) {
        Optional<AgencyArea> optAgencyArea = agencyAreaRepository.findById(id);
        AgencyArea agencyArea = null;
        if (optAgencyArea.isPresent()) {
            agencyArea = optAgencyArea.get();
        } else {
            throw new RuntimeException("Không tìm thấy công ty khu vực " + id);
        }
        System.out.println(agencyArea);
        return agencyArea;
    }

    @Override
    public AgencyArea getAgencyAreaByCode(String areaCode) {
        return agencyAreaRepository.findAgencyAreaByAreaCode(areaCode);
    }

    @Override
    public void deleteAgencyArea(Long id) {
        this.agencyAreaRepository.deleteById(id);
    }

    @Override
    public Page<AgencyArea> searchAgencyArea(String areaCode, String areaName, String taxCode, String description, int page, int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        if ((areaCode != null && !areaCode.equals("")) || (areaName != null && !areaName.equals("")) ||
                (taxCode != null && !taxCode.equals("")) || (description != null && !description.equals(""))) {
            System.out.println("111111111111:"+areaCode);
            System.out.println("222222222222:"+areaName);
            System.out.println("333333333333:"+taxCode);
            System.out.println("444444444444:"+description);
            assert areaCode != null;
            return agencyAreaRepository.findAllArea(areaCode.toUpperCase(), VNCharacterUtils.removeAccent(areaName.toUpperCase()),
                    taxCode.toUpperCase(), VNCharacterUtils.removeAccent(description.toUpperCase()), pageable);
        }
        return agencyAreaRepository.findAll(pageable);
    }

    @Override
    public boolean createAgencyArea(JSONObject data, Principal principal) {

        try {
            AgencyArea agencyArea = new AgencyArea();
            if (!data.getString("areaCode").trim().isEmpty()) {
                agencyArea.setAreaCode(data.getString("areaCode").trim());
            }
            if (!data.getString("areaName").trim().isEmpty()) {
                agencyArea.setAreaName(data.getString("areaName").trim());
            }
            agencyArea.setDescription(data.getString("description").trim());
            agencyArea.setCreateBy(principal.getName());
            agencyArea.setCreateDate(new Date());
            if (!data.getString("taxCode").trim().isEmpty()) {
                agencyArea.setTaxCode(data.getString("taxCode").trim());
            }
            return agencyAreaRepository.saveAndFlush(agencyArea) != null;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean saveAgencyArea(JSONObject data, Principal principal) {
        AgencyArea agencyArea = new AgencyArea();
        try {
            if (!data.getString("areaId").isEmpty()) {
                try {
                    agencyArea = agencyAreaRepository.findAgencyAreaByAreaId(data.getLong("areaId"));
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("areaName").trim().isEmpty()) {
                agencyArea.setAreaName(data.getString("areaName").trim());
            }
            agencyArea.setDescription(data.getString("description").trim());
            agencyArea.setUpdateBy(principal.getName());
            agencyArea.setUpdateDate(new Date());
            if (!data.getString("taxCode").trim().isEmpty()) {
                agencyArea.setTaxCode(data.getString("taxCode").trim());
            }
            return agencyAreaRepository.saveAndFlush(agencyArea) != null;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
