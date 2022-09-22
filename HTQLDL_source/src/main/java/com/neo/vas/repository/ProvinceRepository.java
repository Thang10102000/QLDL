package com.neo.vas.repository;

import com.neo.vas.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province,Long> {

    Province findProvinceById(long id);
}
