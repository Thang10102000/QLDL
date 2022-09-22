package com.neo.vas.service.specification;

import com.neo.vas.domain.Customer;
import com.neo.vas.domain.ServiceRequest;
import com.neo.vas.domain.ServiceRequest_;
import com.neo.vas.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;

/**
 * modifier: YNN
 */

public final class ServiceRequestSpecification {
    @Autowired
    private CustomerService customerService;

    public static Specification<ServiceRequest> hasRequestId(Long requestId) {
        return (root, query, cb) -> cb.equal(root.get(ServiceRequest_.REQUESTID), requestId);
    }

    public static Specification<ServiceRequest> hasStatus(Integer status) {
        return (root, query, cb) -> cb.equal(root.get(ServiceRequest_.STATUS), status);
    }

    public static Specification<ServiceRequest> hasPolicy(String policy) {
        return (root, query, cb) -> cb.equal(root.get(ServiceRequest_.POLICY), policy);
    }

    public static Specification<ServiceRequest> hasPackage(Object packageId) {
        return (root, query, cb) -> cb.equal(root.get("brandASR"), packageId);
    }

    public static Specification<ServiceRequest> hasCustomer(Object customerId) {
        return (root, query, cb) -> cb.equal(root.get("customer"), customerId);
    }

    public static Specification<ServiceRequest> hasAgencyArea(Object areaId) {
        return (root, query, cb) -> cb.equal(root.get("areaId"), areaId);
    }

    public static Specification<ServiceRequest> hasCreatedFrom(Date created) {
        return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(ServiceRequest_.CREATED).as(Date.class),
                created));
    }

    public static Specification<ServiceRequest> hasCreatedTo(Date created) {
        return ((root, query, cb) -> cb.lessThanOrEqualTo(
                cb.function("TRUNC", Date.class, root.get(ServiceRequest_.CREATED).as(Date.class)), created));
    }

    public static Specification<ServiceRequest> hasModifiedFrom(Date modified) {
        return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(ServiceRequest_.MODIFIED).as(Date.class),
                modified));
    }

    public static Specification<ServiceRequest> hasModifiedTo(Date modified) {
        return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(ServiceRequest_.MODIFIED).as(Date.class), modified));
    }

    public static Specification<ServiceRequest> hasApprovedFrom(Date approved) {
        return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(ServiceRequest_.APPROVED).as(Date.class),
                approved));
    }

    public static Specification<ServiceRequest> hasApprovedTo(Date approved) {
        return ((root, query, cb) -> cb.lessThanOrEqualTo(
                cb.function("TRUNC", Date.class, root.get(ServiceRequest_.APPROVED).as(Date.class)), approved));
    }

    public static Specification<ServiceRequest> hasCustomers(List<Customer> customers) {
        return (root, query, cb) -> cb.in(root.get("customer")).value(customers);
    }
}
