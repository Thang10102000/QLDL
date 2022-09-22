package com.neo.vas.service;

import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.domain.LimitDiscount;

import java.util.List;

public interface LimitDiscountService{
    LimitDiscount findLimitDiscountByDiscountCommission(DiscountCommission discount);
    List<LimitDiscount> getLimitByDiscount(long id);
}
