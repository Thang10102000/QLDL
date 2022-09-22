package com.neo.vas.service.impl;

import com.neo.vas.domain.AgencyDiscountCommission;
import com.neo.vas.domain.BrandCommissionPolicy;
import com.neo.vas.repository.*;
import com.neo.vas.service.CommissionAmService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 14/06/2021
 */
@Service
public class CommissionAmServiceImpl implements CommissionAmService {
    @Autowired
    private DiscountCommissionRepository dcRepo;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private AgencyDiscountCommissionRepository agRepo;
    @Autowired
    private BrandCommissionPolicyRepository bcpRepo;
    @Autowired
    private CommissionServiceImpl commissionService;

    @Override
    public Page<AgencyDiscountCommission> searchKamCommission(String commissionId, String agencyId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if (commissionId != null || agencyId != null){
           return agRepo.searchAD(agencyId,commissionId,0,pageable);
        }
        return agRepo.findAll(pageable);
    }

    @Override
    public Page<BrandCommissionPolicy> searchBrandCommission(String commissionId, String brandId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if (commissionId!=null || brandId!=null){
            return bcpRepo.searchBrandCommission(commissionId,brandId,pageable);
        }
        return bcpRepo.findAll(pageable);
    }

    @Override
    public boolean createDetailCommission(JSONObject data, Principal principal){
        try {
            Date date = new Date();
            if (!data.getJSONArray("am-kam").isEmpty()){
                for (Object amKam : data.getJSONArray("am-kam")) {
                    AgencyDiscountCommission newAgKAM = new AgencyDiscountCommission();
                    newAgKAM.setAgencyAD(agencyRepository.getOne(Long.parseLong(amKam.toString())));
                    newAgKAM.setDiscountPolicyAD(dcRepo.getOne(Long.parseLong(data.getString("policyName3"))));
                    newAgKAM.setType(0);
                    agRepo.saveAndFlush(newAgKAM);
                    commissionService.historyAgencyDC(Long.parseLong(amKam.toString()),Long.parseLong(data.getString("policyName3")),0,principal.getName(),date);
                }
            }
            if (!data.getJSONArray("brand").isEmpty()) {
                for (Object br : data.getJSONArray("brand")) {
                    BrandCommissionPolicy brandC = new BrandCommissionPolicy();
                    brandC.setBrandBP(brandRepository.getBrandById(Long.parseLong(br.toString())));
                    brandC.setDiscountCommissionBC(dcRepo.getOne(Long.parseLong(data.getString("policyName3"))));
                    bcpRepo.saveAndFlush(brandC);
                }
            }

            return true;
        }catch (Exception e){
            System.err.println(e);
            return false;
        }
    }

    @Override
    public void deleteKAMCommission(long id) {
        this.agRepo.deleteById(id);
    }

    @Override
    public void deleteBrandCommission(long id){
        this.bcpRepo.deleteById(id);
    }
}
