package com.neo.vas.service.impl;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Brand;
import com.neo.vas.domain.GuaranteePolicy;
import com.neo.vas.domain.SystemConfig;
import com.neo.vas.repository.GuaranteePolicyRepository;
import com.neo.vas.service.GuaranteePolicyService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * @author hai
 *
 */
@Service
public class GuaranteePolicyServiceImpl implements GuaranteePolicyService {
    @Autowired
    private GuaranteePolicyRepository guaranteePolicyRepository;
    @Autowired
    private GuaranteePolicyRepository gpRepo;

    @Override
   public GuaranteePolicy getGuaranteePolicy(Long id) {
        Optional<GuaranteePolicy> optGuaP= guaranteePolicyRepository.findById(id);
        GuaranteePolicy guaP = null;
        if (optGuaP.isPresent()) {
            guaP = optGuaP.get();
        } else {
            throw new RuntimeException("Khong tim thay " + id);
        }
        return guaP;
    }

    @Override
    public boolean saveGuaranteePolicy(int limit) {
        System.out.println(limit);
        GuaranteePolicy guaranteePolicy = new GuaranteePolicy();
        guaranteePolicy = guaranteePolicyRepository.findAll().get(0);
        guaranteePolicy.setLimit(limit);
        return guaranteePolicyRepository.save(guaranteePolicy) != null;
    }

    @Override
    public Integer getLimit() {
        return gpRepo.getLimit();
    }
}
