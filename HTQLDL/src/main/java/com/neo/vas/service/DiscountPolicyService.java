package com.neo.vas.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import com.neo.vas.domain.DiscountCommission;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.Agency;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 19/05/2021
 */
public interface DiscountPolicyService {

    List<DiscountCommission> getAll();

    List<DiscountCommission> getAllDiscountPolicy();

    Page<DiscountCommission> searchDiscountPolicy(String policyName,String isDefault,  String startDate, String endDate, int page, int size);

    boolean createDiscountPolicy(JSONObject data, Principal principal);

    boolean updateDiscountPolicy(JSONObject data, Principal principal);

    DiscountCommission getDiscountPolicyById(Long id);

    void deleteDiscountPolicyById(Long id);

    List<DiscountCommission> getCommission();

    List<Agency> getListAgency(String id);

	List<Agency> getListAgencyAmkam(String id);

    List<Agency> getListAgencyNoPolicy(String id);

    DiscountCommission getPolicyIsDefault(Date startDate , Date endDate, int type);

    DiscountCommission findDiscountEffect(Date now);

    List<DiscountCommission> findCommissionEffect(Date now);
}
