package com.neo.vas.service.impl;

import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.domain.LimitDiscount;
import com.neo.vas.repository.LimitDiscountRepository;
import com.neo.vas.service.LimitDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LimitDiscountServiceImpl implements LimitDiscountService {
    @Autowired
    private LimitDiscountRepository ldRepo;

    @Override
    public LimitDiscount findLimitDiscountByDiscountCommission(DiscountCommission discount) {
        return ldRepo.findLimitDiscountByDiscountCommission(discount);
    }

    @Override
    public List<LimitDiscount> getLimitByDiscount(long id) {
        return ldRepo.getLimitByDiscount(id);
    }
}
