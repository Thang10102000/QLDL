package com.neo.vas.repository;

import com.neo.vas.domain.BrandGroup;
import com.neo.vas.domain.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
@Repository
public interface BrandGroupRepository extends JpaRepository<BrandGroup,Long>, JpaSpecificationExecutor<BrandGroup> {
    BrandGroup getBrandGroupsById(Long id);
//
//    @Query("select p from BrandGroup p where p.servicesBG in (:idService) order by p.groupName")
//    List<BrandGroup> listBrand (List<Services> idService);
//    @Query("select p from BrandGroup p where p.id = ?1")
//    BrandGroup findAllById(Long id);
//
//        @Query("select p.brandGroupB from Brand p where p.id = ?1")
//                BrandGroup findGroupByBrand(Long id);
//
//    BrandGroup findBrandGroupByGroupName(String nameBrand);
//    @Query("select p.brandGroupB from Brand p where p.id = ?1 ")
//    BrandGroup findGroupByBrand(Long id);

    @Query("select p from BrandGroup p where p.servicesBG in (:idService)")
    List<BrandGroup> listBrand (List<Services> idService);
    @Query("select p from BrandGroup p where p.id = ?1")
    BrandGroup findAllById(Long id);
    @Query("select p.brandGroupB from Brand p where p.id = ?1")
    BrandGroup findGroupByBrand(Long id);

    BrandGroup findBrandGroupByGroupName(String nameBrand);

}
