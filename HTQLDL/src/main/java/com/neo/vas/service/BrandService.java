package com.neo.vas.service;

import java.security.Principal;
import java.util.List;

import com.neo.vas.domain.BrandGroup;
import org.json.JSONObject;

import com.neo.vas.domain.Brand;
import org.springframework.data.domain.Page;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
public interface BrandService {
    List<Brand> getAllBrand();

    Page<Brand> searchBrand(Brand brand, int page, int pageSize);

    boolean createBrand(JSONObject data, Principal principal);

    boolean saveBrand(JSONObject data , Principal principal);

    Brand getBrandById(Long id);

    void deleteById(Long id);

    List<Brand> listBrand(String stringListId);

    List<Brand> getListBrandByListBG(List<BrandGroup> brandGroups);
}
