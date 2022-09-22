package com.neo.vas.service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.Brand;
import com.neo.vas.domain.BrandGroup;
import com.neo.vas.domain.BrandGroup_;
import com.neo.vas.domain.Brand_;
import com.neo.vas.domain.Service_;
import com.neo.vas.domain.Services;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
public final class SearchSBG {
//   search service
    public static Specification<Services> hasServiceName(String serviceName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Service_.SERVICENAME).as(String.class)), "%" + serviceName.toLowerCase() + "%");
    }

    public static Specification<Services> hasShortCode(String shortCode) {
        return (root, query, cb) -> cb.like(root.get(Service_.SHORTCODE).as(String.class), "%" + shortCode + "%");
    }

    public static Specification<Services> hasStatus(Integer status) {
        return (root, query, cb) -> cb.equal(root.get(Service_.STATUS), status);
    }

    public static Specification<Services> hasServiceId(String serviceId) {
        return (root, query, cb) -> cb.like(root.get(Service_.SERVICEID).as(String.class),"%"+ serviceId + "%");
    }


//   search brand group

    public static Specification<BrandGroup> hasGroupName(String groupName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(BrandGroup_.GROUPNAME).as(String.class)), "%" + groupName.toLowerCase() + "%");
    }

    public static Specification<BrandGroup> hasStatusBrandGroup(Integer status) {
        return (root, query, cb) -> cb.equal(root.get(BrandGroup_.STATUS), status);
    }

    public static Specification<BrandGroup> hasDescriptionBG(String description) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(BrandGroup_.DESCRIPTION).as(String.class)), "%" + description.toLowerCase() + "%");
    }

    public static Specification<BrandGroup> hasServiceBG(Services serviceBG) {
        return (root, query, cb) -> cb.equal(root.get(BrandGroup_.SERVICEBG), serviceBG);
    }

//   search brand
    public static Specification<Brand> hasBrandId(String brandId) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Brand_.BRANDID).as(String.class)), "%" + brandId.toLowerCase() + "%");
    }

    public static Specification<Brand> hasStatusBrand(Integer status) {
        return (root, query, cb) -> cb.equal(root.get(Brand_.STATUS), status);
    }

    public static Specification<Brand> hasBrandName(String brandName) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(Brand_.BRANDNAME).as(String.class)), "%" + brandName.toLowerCase() + "%");
    }

    public static Specification<Brand> hasBrandType(Integer brandType) {
        return (root, query, cb) -> cb.equal(root.get(Brand_.BRANDTYPE), brandType);

    }
    public static Specification<Brand> hasBrandGroup(BrandGroup brandGroupB) {
        return (root, query, cb) -> cb.equal(root.get(Brand_.BRANDGROUP),  brandGroupB );
    }
}
