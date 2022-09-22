package com.neo.vas.repository;

import com.neo.vas.domain.DiscountCommission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/26/2021
 */
@Repository
public interface DiscountCommissionRepository extends JpaRepository<DiscountCommission, Long> {
    DiscountCommission findById(long id);
    List<DiscountCommission> findByType(int type);
    @Query("select dc from DiscountCommission dc where dc.type = 0 ")
    List<DiscountCommission> getCommission();

//    get policy is default
    @Query("select dc from DiscountCommission dc where dc.isDefault=1 and ((dc.startDate <= ?1 and dc.endDate >= ?2) or (dc.startDate >= ?1 and dc.startDate <= ?2 and dc.endDate >= ?2) " +
            " or (dc.startDate <= ?1 and dc.endDate >= ?1 and dc.endDate <= ?2)) and dc.type = ?3 ")
    DiscountCommission getPolicyIsDefault(Date startDate , Date endDate, int type);


    //    search theo cả ngày bắt đầu và ngày kết thúc
    @Query("select c from DiscountCommission c where  lower(c.policyName) LIKE %?1% "
            + "AND (?2 is null or c.isDefault = ?2) and  c.startDate >= ?3 and  c.endDate <= ?4 and c.type = ?5 order by c.createdDate desc ")
    Page<DiscountCommission> findByAll(String policyName, String isDefault, Date startDate, Date endDate, int type, Pageable pageable);

    //    ngày kết thúc null
    @Query("select c from DiscountCommission c where lower(c.policyName) LIKE %?1%"
            + "AND (?2 is null or c.isDefault = ?2) and c.startDate >= ?3 and c.type = ?4 order by c.createdDate desc ")
    Page<DiscountCommission> findEndDateIsNull(String policyName, String isDefault, Date startDate,int type, Pageable pageable);

    //   ngày bắt đầu null
    @Query("select c from DiscountCommission c where lower(c.policyName) LIKE %?1%"
            + "AND (?2 is null or c.isDefault = ?2) and c.endDate >= ?3 and c.type = ?4 order by c.createdDate desc ")
    Page<DiscountCommission> findStartDateIsNull(String policyName, String isDefault, Date endDate,int type, Pageable pageable);

    //   ngày bắt đầu, ngày kết thúc null
    @Query("select c from DiscountCommission c where lower(c.policyName) LIKE %?1%"
            + "AND (?2 is null or c.isDefault = ?2) and c.type = ?3 order by c.createdDate desc ")
    Page<DiscountCommission> findStartEndIsNull(String policyName, String isDefault,int type, Pageable pageable);
    @Query("select dc from DiscountCommission dc where dc.type = ?1 ")
    Page<DiscountCommission> getAll(int type, Pageable pageable);


    @Query("select dc from DiscountCommission dc where dc.isDefault=1 and (dc.startDate <= ?1 and dc.endDate >= ?1) and dc.type = 1 ")
    DiscountCommission findDiscountEffect(Date now);
    @Query("select dc from DiscountCommission dc where (dc.startDate <= ?1 and dc.endDate >= ?1) and dc.type = 0 ")
    List<DiscountCommission> findCommissionEffect(Date now);
}
