package com.neo.vas.service.impl;

import com.neo.vas.domain.AgencyDiscountCommission;
import com.neo.vas.repository.AgencyDiscountCommissionRepository;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.DiscountCommissionRepository;
import com.neo.vas.service.AgencyDiscountService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Date;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 12/06/2021
 */
@Service
public class AgencyDiscountServiceImpl implements AgencyDiscountService {
    @Autowired
    private AgencyDiscountCommissionRepository adRepository;
    @Autowired
    private DiscountCommissionRepository dpRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private CommissionServiceImpl commissionService;

    @Override
    @Transactional(readOnly = true)
    public Page<AgencyDiscountCommission> searchAgencyDiscount(String agencyName, String policyName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (agencyName != null || policyName != null) {
            return adRepository.searchAD(agencyName, policyName,1, pageable);
        }
        return adRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public boolean createAgencyDiscount(JSONObject data, Principal principal) {
        try {
            Date date = new Date();
            if (!data.getString("policyName2").isEmpty() && !data.getJSONArray("agency").isEmpty()) {
                for (Object ag : data.getJSONArray("agency")) {
                    AgencyDiscountCommission agencyDiscount = new AgencyDiscountCommission();
                    agencyDiscount.setDiscountPolicyAD(dpRepository.getOne(Long.parseLong(data.getString("policyName2"))));
                    agencyDiscount.setAgencyAD(agencyRepository.getOne(Long.parseLong(ag.toString())));
                    agencyDiscount.setType(1);
                    adRepository.saveAndFlush(agencyDiscount);
                    commissionService.historyAgencyDC(Long.parseLong(ag.toString()),Long.parseLong(data.getString("policyName2")),1,principal.getName(),date);
                }
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean editAgencyDiscount(JSONObject data, Principal principal) {
        return false;
    }

    @Override
    public void deleteAgencyDiscount(long id) {
        this.adRepository.deleteById(id);
    }
}
