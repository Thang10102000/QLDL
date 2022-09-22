package com.neo.vas.service;

import com.neo.vas.domain.AgencyDiscountCommission;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.security.Principal;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 12/06/2021
 */
public interface AgencyDiscountService {
    Page<AgencyDiscountCommission> searchAgencyDiscount(String agencyName, String policyName, int page, int size);
    boolean createAgencyDiscount(JSONObject data, Principal principal);
    boolean editAgencyDiscount(JSONObject data, Principal principal);
    void deleteAgencyDiscount(long id);
}
