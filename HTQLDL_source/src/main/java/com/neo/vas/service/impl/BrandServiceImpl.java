package com.neo.vas.service.impl;

import java.security.Principal;
import java.util.*;

import com.neo.vas.domain.AgencyArea;
import com.neo.vas.domain.BrandGroup;
import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.repository.DiscountCommissionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.Brand;
import com.neo.vas.repository.BrandGroupRepository;
import com.neo.vas.repository.BrandRepository;
import com.neo.vas.service.BrandService;
import com.neo.vas.service.specification.SearchSBG;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private BrandGroupRepository brandGroupRepository;
    @Autowired
    private BrandGroupServiceImpl brandGroupServiceImpl;
    @Autowired
    private DiscountCommissionRepository dcRepo;

    @Override
    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    Date date = new Date();
    @Override
    public Page<Brand> searchBrand(Brand brand, int page, int pageSize) {
        Specification<Brand> conditions = Specification
                .where((brand.getBrandId().isEmpty()) ? null : SearchSBG.hasBrandId(brand.getBrandId()))
                .and((brand.getBrandName().isEmpty()) ? null : SearchSBG.hasBrandName(brand.getBrandName()))
                .and((brand.getStatus() != 100) ? SearchSBG.hasStatusBrand(brand.getStatus()) : null )
                .and((brand.getBrandType() != 100) ?  SearchSBG.hasBrandType(brand.getBrandType()) : null)
                .and(brand.getBrandGroupB() != null ? SearchSBG.hasBrandGroup(brand.getBrandGroupB()):null);
        Page<Brand> pageBrand = brandRepository.findAll(conditions, PageRequest.of(page,pageSize, Sort.by("createdDate").descending()));
        for (Brand b : pageBrand){
            if (b.getDcPolicyId() != null){
                DiscountCommission dc = dcRepo.findById(b.getDcPolicyId()).get();
                b.setDcPolicyId((long) dc.getRate());
            }
        }
        return pageBrand;
    }

    @Override
    @Transactional
    public boolean createBrand(JSONObject data, Principal principal) {
        try {
            Brand brand = new Brand();
            if (!data.getString("brandId").isEmpty()) {
                brand.setBrandId(data.getString("brandId").trim());
            }
            if (!data.getString("brandName").isEmpty()) {
                brand.setBrandName(data.getString("brandName"));
            }
            if (!data.getString("brandType").isEmpty()) {
                brand.setBrandType(Integer.parseInt(data.getString("brandType").trim()));
            }
            if (!data.getString("status").isEmpty()) {
                brand.setStatus(Integer.parseInt(data.getString("status").trim()));
            }
            if (!data.getString("activeDay").isEmpty()) {
                brand.setActiveDay(Integer.parseInt(data.getString("activeDay").trim()));
            }
            if (!data.getString("approvedBy").isEmpty()) {
                brand.setApprovedBy(Long.parseLong(data.getString("approvedBy").trim()));
            }
            if (!data.getString("brandGroup").isEmpty()) {
                try {
                    brand.setBrandGroupB(brandGroupRepository.getOne(Long.parseLong(data.getString("brandGroup"))));
                } catch (Exception e) {
                    System.err.println(e);
                    return false;
                }
            } else {
                return false;
            }
            if(Integer.parseInt(data.getString("typeDiscountCommission")) == 0) {
                if (data.has("tlfor") && !data.getString("commissionRate").isEmpty() && data.has("commissionRate")) {
                    brand.setCommissionRate(Integer.parseInt(data.getString("commissionRate").trim()));
                }
                if (!data.has("tlfor") && data.has("dcPolicyId") && !data.getString("dcPolicyId").isEmpty()) {
                    brand.setDcPolicyId(Long.parseLong(data.getString("dcPolicyId").trim()));
                    brand.setCommissionRate(0);
                }
                if (!data.getString("priceBrand").isEmpty()) {
                    String priceBrand = data.getString("priceBrand").trim();
                    priceBrand = priceBrand.replaceAll("[^a-zA-Z0-9]", "");
                    brand.setPrice(Long.parseLong(priceBrand));
                }
            }
            else {
                if (!data.getString("priceBrand").isEmpty()) {
                    String priceBrand = data.getString("priceBrand").trim();
                    priceBrand = priceBrand.replaceAll("[^a-zA-Z0-9]", "");
                    brand.setPrice(Long.parseLong(priceBrand));
                }
                if (!data.getString("discountRate").isEmpty()) {
                    brand.setDiscountRate(Integer.parseInt(data.getString("discountRate").trim()));
                }
                if (!data.getString("priceDiscount").isEmpty()) {
//                    try {
                        brand.setPriceDiscount(Long.parseLong(data.getString("priceDiscount").trim()));
//                    } catch (NumberFormatException nfe) {
//                        long money = Long.parseLong(data.getString("priceBrand"));
//                        int discount = Integer.parseInt(data.getString("discountRate").trim());
//                        brand.setPriceDiscount(money * discount / 100);
//                    }
                }
            }
            brand.setDescription(data.getString("description").trim());
            brand.setCreatedBy(principal.getName());
            brand.setCreatedDate(date);
            brandRepository.saveAndFlush(brand);
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean saveBrand(JSONObject data, Principal principal) {
        System.out.println(data);
        Brand brand1 = new Brand();
        try {
            if (!data.getString("id").isEmpty()) {
                try {
                    brand1 = brandRepository.getBrandById(Long.parseLong(data.getString("id")));
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            } else {
                return false;
            }
            if (!data.getString("brandId").trim().isEmpty()) {
                brand1.setBrandName(data.getString("brandId").trim());
            }
            if (!data.getString("brandName").trim().isEmpty()) {
                brand1.setBrandName(data.getString("brandName").trim());
            }
            if (!data.getString("brandType").trim().isEmpty()) {
                brand1.setBrandType(Integer.parseInt(data.getString("brandType").trim()));
            }
            if (!data.getString("status").trim().isEmpty()) {
                brand1.setStatus(Integer.parseInt(data.getString("status").trim()));
            }
            if (!data.getString("activeDay").trim().isEmpty()) {
                brand1.setActiveDay(Integer.parseInt(data.getString("activeDay").trim()));
            }
            //
            if (!data.getString("approvedBy").trim().isEmpty()) {
                brand1.setApprovedBy(Long.parseLong(data.getString("approvedBy").trim()));
            }
            if (!data.getString("brandGroupB").trim().isEmpty()) {
                try {
                    brand1.setBrandGroupB(brandGroupRepository.getOne(Long.parseLong(data.getString("brandGroupB"))));
                } catch (Exception e) {
                    System.out.println(e);
                    return false;
                }
            }
             else {
                return false;
            }

            if (data.getString("price") == "0" ) {
                brand1.setPrice(0);
                brand1.setDiscountRate(0);
                brand1.setPriceDiscount(0);
                brand1.setCommissionRate(0);
                brand1.setDcPolicyId(null);
            } else {
                if (Integer.parseInt(data.getString("typeDiscountCommission")) == 0) {
                    if (data.has("tlfor") && !data.getString("commissionRate").isEmpty() && data.has("commissionRate")) {
                        brand1.setCommissionRate(Integer.parseInt(data.getString("commissionRate").trim()));
                        brand1.setDcPolicyId(null);
                        brand1.setPriceDiscount(0);
                        brand1.setDiscountRate(0);
                    }
                    if (!data.has("tlfor") && data.has("dcPolicyId") && !data.getString("dcPolicyId").isEmpty()) {
                        brand1.setDcPolicyId(Long.parseLong(data.getString("dcPolicyId").trim()));
                        brand1.setCommissionRate(0);
                        brand1.setPriceDiscount(0);
                        brand1.setDiscountRate(0);
                    }
                    if (!data.getString("price").isEmpty()) {
                        String priceBrand = data.getString("price").trim();
                        priceBrand = priceBrand.replaceAll("[^a-zA-Z0-9]", "");
                        brand1.setPrice(Long.parseLong(priceBrand));
                    }
                } else {
                    if (!data.getString("price").isEmpty()) {
                        String priceBrand = data.getString("price").trim();
                        priceBrand = priceBrand.replaceAll("[^a-zA-Z0-9]", "");
                        brand1.setPrice(Long.parseLong(priceBrand));
                    }
                    if (!data.getString("discountRate").isEmpty()) {
                        brand1.setDiscountRate(Integer.parseInt(data.getString("discountRate").trim()));
                        brand1.setCommissionRate(0);
                        brand1.setDcPolicyId(null);
                    }
                    if (!data.getString("priceDiscount").isEmpty()) {
//                        try {
                            brand1.setPriceDiscount(Long.parseLong(data.getString("priceDiscount").trim()));
//                        } catch (NumberFormatException nfe) {
//                            long money = Long.parseLong(data.getString("price"));
//                            int discount = Integer.parseInt(data.getString("discountRate").trim());
//                            brand1.setPriceDiscount(money * discount / 100);
//                        }
                    }
                }
            }
            brand1.setDescription(data.getString("description").trim());
            brand1.setUpdateBy(principal.getName());
            brand1.setUpdateDate(date);
            brandRepository.saveAndFlush(brand1);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Brand getBrandById(Long id) {
        Optional<Brand> optBrand = brandRepository.findById(id);
        Brand brand = null;
        if (optBrand.isPresent()) {
            brand = optBrand.get();
        } else {
            throw new RuntimeException("Không tìm thấy gói cước " + id);
        }
        return brand;
    }


    @Override
    public void deleteById(Long id) {
        this.brandRepository.deleteById(id);
    }
    @Override
    public List<Brand> listBrand(String stringListId) {
        List<BrandGroup> lstBrandGroup = new ArrayList<>();
        String[] listBrandG = stringListId.split(",");
        for (String strId : listBrandG) {
            try {
                Long idBrandG = Long.parseLong(strId);
                BrandGroup brandG = brandGroupServiceImpl.getBrandGroupById(idBrandG);
                lstBrandGroup.add(brandG);
            } catch (Exception e) {
                return null;
            }
        }
        List<Brand> lstBG = brandRepository.findByBrandGroupBIn(lstBrandGroup);
        Comparator<Brand> brandComparator = Comparator.comparing(Brand::getBrandName);
        lstBG.sort(brandComparator);
        return lstBG;
    }

    @Override
    public List<Brand> getListBrandByListBG(List<BrandGroup> brandGroups) {
        return brandRepository.findByBrandGroupBIn(brandGroups);
    }
}
