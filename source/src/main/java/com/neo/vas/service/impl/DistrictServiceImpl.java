package com.neo.vas.service.impl;

import com.neo.vas.domain.District;
import com.neo.vas.repository.DistrictRepository;
import com.neo.vas.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<District> getAllDistrict() {
        List<District> districtList = districtRepository.findAll();
        Comparator<District> districtComparator = Comparator.comparing(District::getDistrictName);
        districtList.sort(districtComparator);
        return districtList;
    }

    @Override
    public List<District> getDistrictByProvinceId(Long provinceId) {
        return districtRepository.lstDistrict(provinceId);
    }
}
