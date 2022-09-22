package com.neo.vas.service.impl;

import com.neo.vas.domain.Ward;
import com.neo.vas.repository.WardRepository;
import com.neo.vas.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private WardRepository wardRepository;

    @Override
    public List<Ward> getAllWard() {
        List<Ward> wardList = wardRepository.findAll();
        Comparator<Ward> wardComparator = Comparator.comparing(Ward::getWardName);
        wardList.sort(wardComparator);
        return wardList;
    }

    @Override
    public List<Ward> getWardByDistrictId(Long districtId) {
        return wardRepository.lstWard(districtId);
    }

    @Override
    public Ward findWardById(long id) {
        return wardRepository.findWardById(id);
    }
}
