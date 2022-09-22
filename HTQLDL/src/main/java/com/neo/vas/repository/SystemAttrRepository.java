package com.neo.vas.repository;

import com.neo.vas.domain.SystemAttr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAttrRepository extends JpaRepository<SystemAttr, Long>, JpaSpecificationExecutor<SystemAttr> {
    @Query("select ag from SystemAttr ag where concat(ag.saId) like %?1% and FN_CONVERT_TO_VN(upper(concat(ag.type))) like %?2% and FN_CONVERT_TO_VN(upper(concat(ag.name))) like %?3% "
            + " and FN_CONVERT_TO_VN(upper(concat(ag.value))) like %?4% and FN_CONVERT_TO_VN(upper(concat(ag.description))) like %?5% ")
    Page<SystemAttr> findSystemAttr(String saId, String type, String name, String value, String description, Pageable pageable);

    @Query("select ag from SystemAttr ag where ag.type = :type and ag.name = :name ")
    SystemAttr findSystemAttrbyTypeName(String type, String name);
}
