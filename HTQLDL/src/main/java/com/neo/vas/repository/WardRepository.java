package com.neo.vas.repository;

import com.neo.vas.domain.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward,Long> {
    @Query("select wa from Ward wa where wa.districtId.id = ?1 ")
    List<Ward> lstWard(Long districtId);

    Ward findWardById(long id);
}
