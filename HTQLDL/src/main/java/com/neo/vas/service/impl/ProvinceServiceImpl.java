package com.neo.vas.service.impl;

import com.neo.vas.domain.Province;
import com.neo.vas.repository.ProvinceRepository;
import com.neo.vas.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<Province> getAllProvince() {
        List<Province> provinceList = provinceRepository.findAll();
        Comparator<Province> provinceComparator = Comparator.comparing(Province::getProvince_name);
        provinceList.sort(provinceComparator);
        return provinceList;
    }

    @Override
    public Province getProvinceById(Long id) {
        Optional<Province> optionalProvince = provinceRepository.findById(id);
        Province province = null;
        if (optionalProvince.isPresent()){
            province = optionalProvince.get();
        }else {
            throw new RuntimeException("Không tìm thấy tỉnh thành" + id);
        }
        return province;
    }
}
