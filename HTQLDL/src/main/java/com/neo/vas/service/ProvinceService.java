package com.neo.vas.service;

import com.neo.vas.domain.Province;

import java.util.List;

public interface ProvinceService {
    List<Province> getAllProvince();
    Province getProvinceById(Long id);
}
