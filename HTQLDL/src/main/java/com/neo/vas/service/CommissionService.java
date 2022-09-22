package com.neo.vas.service;


import java.security.Principal;
import java.util.List;

import com.neo.vas.domain.*;
import org.json.JSONObject;
import org.springframework.data.domain.Page;


/**
 * project_name: demo
 * Created_by: thulv
 * time: 17/05/2021
 */
public interface CommissionService {
    List<DiscountCommission> getALlCommission();

    Page<DiscountCommission> searchCommissionData(String policyName, String isDefault, String startDate, String endDate,int type, int page, int size);

    boolean createCommission(JSONObject data, Principal principal);

    boolean updateCommission(JSONObject data, Principal principal);

    DiscountCommission getCommissionById(Long id);

    List<DiscountCommission> getListDCByType(int type);

    void deleteCommission(Long id);

    List<Brand> listBrand(String stringListId);

    List<BrandGroup> listBrandG(String stringList);

    List<Agency> listAmKam(String areaId);
}
