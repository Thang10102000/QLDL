package com.neo.vas.service;

import com.neo.vas.domain.GuaranteePolicy;
import org.json.JSONObject;

import java.security.Principal;
import java.util.List;
/**
 * @author hai
 *
 */
public interface GuaranteePolicyService {
    GuaranteePolicy getGuaranteePolicy(Long id);
    boolean saveGuaranteePolicy(int limit);
    Integer getLimit();
}
