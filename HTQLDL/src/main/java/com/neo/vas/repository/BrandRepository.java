package com.neo.vas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.Brand;
import com.neo.vas.domain.BrandGroup;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand , Long>, JpaSpecificationExecutor<Brand> {
    Brand getBrandById(Long id);

    List<Brand> findByBrandGroupBIn(List<BrandGroup> idBrandG);

    @Query("select p from Brand p where p.brandGroupB.groupName like %?1%")
    List<Brand> findAllByGroupName(String gn);

    Brand findByBrandName(String nameBrand);
}
