package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(ServiceRequest.class)
public class ServiceRequest_ {
	public static volatile SingularAttribute<ServiceRequest_, Long> srId;
	public static volatile SingularAttribute<ServiceRequest_, String> description;
	public static volatile SingularAttribute<ServiceRequest_, String> policy;
	public static volatile SingularAttribute<ServiceRequest_, Integer> status;
	public static volatile SingularAttribute<ServiceRequest_, Long> price;
	public static volatile SingularAttribute<ServiceRequest_, Long> discountCost;
	public static volatile SingularAttribute<ServiceRequest_, Long> quantity;
	public static volatile SingularAttribute<ServiceRequest_, Long> amount;
	public static volatile SingularAttribute<ServiceRequest_, String> creator;
    public static volatile SingularAttribute<ServiceRequest_, String> created;
    public static volatile SingularAttribute<ServiceRequest_, String> modifier;
    public static volatile SingularAttribute<ServiceRequest_, String> modified;
    public static volatile SingularAttribute<ServiceRequest_, String> approved;

    public static final String REQUESTID = "srId";
    public static final String POLICY = "policy";
    public static final String DESCRIPTION = "description";
    public static final String STATUS = "status";
    public static final String PRICE = "price";
    public static final String DISCOUNTCOST = "discountCost";
    public static final String QUANTITY = "amount";
    public static final String CREATOR = "creator";
    public static final String MODIFIER = "modified";
    public static final String CREATED = "created";
    public static final String MODIFIED = "modified";
    public static final String APPROVED = "approved";

}
