package com.neo.vas.service;

import com.neo.vas.domain.AgencyDiscountCommission;
import com.neo.vas.domain.BrandCommissionPolicy;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 14/06/2021
 */
public interface CommissionAmService {
    Page<AgencyDiscountCommission> searchKamCommission(String commissionId, String agencyId, int page, int size);
    Page<BrandCommissionPolicy> searchBrandCommission(String commissionId, String brandId, int page, int size);
    boolean createDetailCommission(JSONObject data, Principal principal);
    void deleteKAMCommission(long id);
    void deleteBrandCommission(long id);
}
