//package com.neo.vas.service.specification;
//
//import org.springframework.data.jpa.domain.Specification;
//
//import com.neo.vas.domain.CommisionPolicy;
//import com.neo.vas.domain.CommissionPolicy_;
//
//import java.util.Date;
//
///**
// * project_name: demo
// * Created_by: thulv
// * time: 17/05/2021
// */
//public final class CommissionSpecification {
//    public static Specification<CommisionPolicy> hasCommissionName(String policyName) {
//        return (root, query, cb) -> cb.like(root.get(CommissionPolicy_.POLICYNAME).as(String.class), "%" +  policyName + "%") ;
//    }
//    public static Specification<CommisionPolicy> hasCommissionRate(Integer commissionRate) {
//        return (root, query, cb) -> cb.equal(root.get(CommissionPolicy_.COMMISSIONRATE), commissionRate);
//    }
//    public static Specification<CommisionPolicy> hasStartDate(Date startDate) {
//        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(CommissionPolicy_.STARTDATE).as(Date.class), startDate);
//    }
//    public static Specification<CommisionPolicy> hasEndDate(Date endDate) {
//        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(CommissionPolicy_.ENDDATE).as(Date.class), endDate);
//    }
//}
