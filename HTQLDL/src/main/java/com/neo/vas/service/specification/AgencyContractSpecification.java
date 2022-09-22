package com.neo.vas.service.specification;


import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyContract;
import com.neo.vas.domain.AgencyContract_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 10/06/2021
 */
public final class AgencyContractSpecification {
    public static Specification<AgencyContract> hasContractNo(String contractNo){
        return ((root, query, cb) -> cb.like(root.get(AgencyContract_.CONTRACTNO).as(String.class), "%" + contractNo + "%"));
    }
    public static Specification<AgencyContract> hasAgencyName(Agency agencyName){
        return ((root, query, cb) -> cb.equal(root.get(AgencyContract_.AGENCYAC), agencyName));
    }
    public static Specification<AgencyContract> hasStatus(Integer status){
        return ((root, query, cb) -> cb.equal(root.get(AgencyContract_.STATUS), status));
    }
    public static Specification<AgencyContract> hasServiceType(String serviceType){
        return ((root, query, cb) -> cb.like(root.get(AgencyContract_.SERVICETYPE), serviceType));
    }
//    ngày hiệu lực
    public static Specification<AgencyContract> hasStartDate(Date startDate){
        return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(AgencyContract_.STARTDATE).as(Date.class), startDate));
    }
    public static Specification<AgencyContract> hasEndDate(Date endDate){
        return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(AgencyContract_.ENDDATE).as(Date.class), endDate));
    }
    public static Specification<AgencyContract> hasStartEnd(Date startDate, Date endDate){
        return ((root, query, cb) -> cb.and((cb.greaterThanOrEqualTo(root.get(AgencyContract_.STARTDATE).as(Date.class), startDate)),
                (cb.lessThanOrEqualTo(root.get(AgencyContract_.ENDDATE).as(Date.class), endDate))));
    }
}
