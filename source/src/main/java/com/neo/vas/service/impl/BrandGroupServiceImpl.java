package com.neo.vas.service.impl;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.neo.vas.domain.Services;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.BrandGroup;
import com.neo.vas.repository.BrandGroupRepository;
import com.neo.vas.repository.ServiceRepository;
import com.neo.vas.service.BrandGroupService;
import com.neo.vas.service.specification.SearchSBG;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
@Service
public class BrandGroupServiceImpl implements BrandGroupService {

    @Autowired
    private BrandGroupRepository brandGroupRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public List<BrandGroup> getAllBrandGroup() {
        return brandGroupRepository.findAll();
    }

    Date date = new Date();
    @Override
    @Transactional(readOnly = true)
    public Page<BrandGroup> serachBrandGroup(BrandGroup brandGroup, int page, int pageSize) {
        Specification<BrandGroup> conditions = Specification
                .where((brandGroup.getGroupName().isEmpty()) ? SearchSBG.hasGroupName("")
                        : SearchSBG.hasGroupName(brandGroup.getGroupName()))
                .and((null != brandGroup.getServicesBG()) ? SearchSBG.hasServiceBG(brandGroup.getServicesBG()) : null)
                .and((brandGroup.getStatus() == 100) ? null : SearchSBG.hasStatusBrandGroup(brandGroup.getStatus()));
        Page<BrandGroup> pageBG = brandGroupRepository.findAll(conditions, PageRequest.of(page,pageSize, Sort.by("createdDate").descending()));
        return pageBG;
    }

    @Override
    public boolean createdBrandGroup(JSONObject data, Principal principal) {
        BrandGroup brandGroup = new BrandGroup();
        try {
            if (!data.getString("brandGroupName").trim().isEmpty()) {
                brandGroup.setGroupName(data.getString("brandGroupName").trim());
            }
            if (!data.getString("service").isEmpty()) {
                try {
                    brandGroup.setServicesBG(serviceRepository.getOne(Long.parseLong(data.getString("service"))));
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            brandGroup.setStatus(0);
            brandGroup.setDescription(data.getString("description").trim());
            brandGroup.setCreatedBy(principal.getName());
            brandGroup.setCreatedDate(date);
            return brandGroupRepository.saveAndFlush(brandGroup) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean updateBrandGroup(JSONObject data, Principal principal) {
        BrandGroup brandGroup1 = new BrandGroup();
        try {
            if (!data.getString("id").isEmpty()) {
                try {
                    brandGroup1 = brandGroupRepository.getBrandGroupsById(data.getLong("id"));
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("groupName").isEmpty()) {
                brandGroup1.setGroupName(data.getString("groupName").trim());
            }
            if (!data.getString("servicesBG").isEmpty()) {
                try {
                    brandGroup1.setServicesBG(serviceRepository.getOne(Long.parseLong(data.getString("servicesBG"))));
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("status").isEmpty()) {
                brandGroup1.setStatus(Integer.parseInt(data.getString("status")));
            }
            brandGroup1.setDescription(data.getString("description").trim());
            brandGroup1.setUpdateBy(principal.getName());
            brandGroup1.setUpdateDate(date);
            return brandGroupRepository.saveAndFlush(brandGroup1) != null;
        } catch (Exception e) {

        }
        return false;
    }


    @Override
    public BrandGroup getBrandGroupById(Long id) {
        Optional<BrandGroup> optBrandGroup = brandGroupRepository.findById(id);
        BrandGroup brandGroup = null;
        if (optBrandGroup.isPresent()) {
            brandGroup = optBrandGroup.get();
        } else {
            throw new RuntimeException("Không tìm thấy nhóm gói cước " + id);
        }
        return brandGroup;
    }

    @Override
    public List<BrandGroup> searchBrandGByServiceId(String serviceId) {
        List<BrandGroup> lstBrandG = null;
        try{
            if (!serviceId.isEmpty()){
//                lstBrandG = brandGroupRepository.listBrandGroup(Long.parseLong(serviceId));
            }
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
        return lstBrandG;
    }

    @Override
    public List<BrandGroup> getListBGByListService(List<Services> services) {
        return brandGroupRepository.listBrand(services);
    }

    @Override
    public void deleteBrandGroup(Long id) {
        this.brandGroupRepository.deleteById(id);
    }

    @Override
    public BrandGroup getBGByBrandId(Long id) {
        return brandGroupRepository.findGroupByBrand(id);
    }
}
