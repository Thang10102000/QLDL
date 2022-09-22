package com.neo.vas.service.specification;

import com.neo.vas.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 27/05/2021
 */
public final class AgencySpecification {
	public static Specification<Agency> hasAgencyName(String agencyName) {
		return (root, query, cb) -> cb.like(cb.function("FN_CONVERT_TO_VN", String.class,
				cb.upper(root.get("agencyName").as(String.class))),
				"%" + agencyName.toUpperCase() + "%");
	}
    public static Specification<Agency> hasStatus(Integer status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
    public static Specification<Agency> hasAreaId(AgencyArea areaId) {
        return (root, query, cb) -> cb.equal(root.get("areaId"), areaId);
    }
    public static Specification<Agency> hasAgencyType(Integer agencyType) {
        return (root, query, cb) -> cb.equal(root.get("agencyType"), agencyType);
    }
    
    public static Specification<Agency> hasType(Integer type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }
    
    public static Specification<Agency> hasAgencyCode(String agencyCode) {
		return (root, query, cb) -> cb.like(cb.function("FN_CONVERT_TO_VN", String.class,
				cb.upper(root.get("agencyCode").as(String.class))),
				"%" + agencyCode.toUpperCase() + "%");
	}

//	AGENCY ORDER
	public static Specification<AgencyOrders> hasAgencyOrder(Long orderId){
	    return ((root, query, cb) -> cb.equal(root.get(AgencyOrder_.ORDERID), orderId));
    }
    public static Specification<AgencyOrders> hasAgency(Agency agencyAO){
        return ((root, query, cb) -> cb.equal(root.get(AgencyOrder_.AGENCYAO), agencyAO));
    }
    public static Specification<AgencyOrders> hasAgencyOStatus(OrderStatus orderStatusAO){
	    return ((root, query, cb) -> cb.equal(root.get(AgencyOrder_.ORDERSTATUSAO), orderStatusAO));
    }
    public static Specification<AgencyOrders> hasStartDate(Date startDate){
	    return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(AgencyOrder_.STARTDATE).as(Date.class), startDate));
    }
    public static Specification<AgencyOrders> hasEndDate(Date endDate){
	    return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(AgencyOrder_.ENDDATE).as(Date.class), endDate));
    }
    public static Specification<AgencyOrders> hasStartEnd(Date startDate, Date endDate){
	    return ((root, query, cb) -> cb.and(cb.greaterThanOrEqualTo(root.get(AgencyOrder_.STARTDATE).as(Date.class), startDate),
                cb.lessThanOrEqualTo(root.get(AgencyOrder_.ENDDATE).as(Date.class), endDate)));
    }
    public static Specification<AgencyOrders> hasPaymentMethodOrders(Long paymentMethod){
        return ((root, query, cb) -> cb.equal(root.get(AgencyOrder_.PAYMENTMETHOD), paymentMethod));
    }
    public static Specification<AgencyOrders> hasAgencys(List<Agency> agencyList) {
        return (root, query, cb) -> cb.in(root.get(AgencyOrder_.AGENCYAO)).value(agencyList);
    }

//    AGENCY ORDER PAYMENT
    public static Specification<AgencyOrderPayment> hasAgencyOrderId(AgencyOrders agencyOrdersAOP){
        return ((root, query, cb) -> cb.equal(root.get(AgencyOrderPayment_.AGENCYORDERSAOP), agencyOrdersAOP));
    }
    public static Specification<AgencyOrderPayment> hasPaymentMethod(String paymentMethod){
        return ((root, query, cb) -> cb.like(cb.lower(root.get(AgencyOrderPayment_.PAYMENTMETHOD).as(String.class)), "%" + paymentMethod.toLowerCase() + "%"));
    }
    public static Specification<AgencyOrderPayment> hasAmount(Long amount){
        return ((root, query, cb) -> cb.equal(root.get(AgencyOrderPayment_.AMOUNT), amount));
    }
    public static Specification<AgencyOrderPayment> hasReceiverName(String receiverName){
        return ((root, query, cb) -> cb.like(cb.lower(root.get(AgencyOrderPayment_.RECEIVERNAME).as(String.class)), "%"+ receiverName.toLowerCase() + "%"));
    }
    public static Specification<AgencyOrderPayment> hasReceiverTime(Date receiverTime){
        return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(AgencyOrderPayment_.RECEIVERTIME).as(Date.class), receiverTime));
    }
}
