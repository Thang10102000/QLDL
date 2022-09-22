package com.neo.vas.repository;

import com.neo.vas.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District,Long> {

    @Query("select di from District di where di.provinceId.id = ?1 ")
    List<District> lstDistrict(Long provinceId);

    District findDistrictById(long id);
}
