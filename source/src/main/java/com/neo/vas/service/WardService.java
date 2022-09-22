package com.neo.vas.service;

import com.neo.vas.domain.Ward;

import java.util.List;

public interface WardService {
    List<Ward> getAllWard();
    List<Ward> getWardByDistrictId(Long districtId);
    Ward findWardById(long id);
}
