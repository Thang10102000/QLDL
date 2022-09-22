package com.neo.vas.service;

import com.neo.vas.domain.District;

import java.util.List;

public interface DistrictService {
    List<District> getAllDistrict();
    List<District> getDistrictByProvinceId(Long provinceId);
}
