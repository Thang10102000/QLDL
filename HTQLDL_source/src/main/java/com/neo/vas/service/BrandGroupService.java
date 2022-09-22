package com.neo.vas.service;

import com.neo.vas.domain.BrandGroup;
import com.neo.vas.domain.Services;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
public interface BrandGroupService {

    List<BrandGroup> getAllBrandGroup();

    Page<BrandGroup> serachBrandGroup(BrandGroup brandGroup, int page, int pageSize);

    boolean createdBrandGroup(JSONObject data, Principal principal);

    boolean updateBrandGroup(JSONObject data, Principal principal);

    BrandGroup getBrandGroupById(Long id);

    List<BrandGroup> searchBrandGByServiceId(String serviceId);

    List<BrandGroup> getListBGByListService(List<Services> services);
    void deleteBrandGroup(Long id);

    BrandGroup getBGByBrandId(Long id);
}
