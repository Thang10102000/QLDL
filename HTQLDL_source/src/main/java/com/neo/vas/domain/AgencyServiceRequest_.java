package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;


@StaticMetamodel(AgencyServiceRequest.class)
public class AgencyServiceRequest_ {
	public static volatile SingularAttribute<AgencyServiceRequest_, Long> requestId;
	public static volatile SingularAttribute<AgencyServiceRequest_, Long> totalValue;
	public static volatile SingularAttribute<AgencyServiceRequest_, String> description;
	public static volatile SingularAttribute<AgencyServiceRequest_, String> customerAccount;
	public static volatile SingularAttribute<AgencyServiceRequest_, Integer> status;
	public static volatile SingularAttribute<AgencyServiceRequest_, String> message;
	public static volatile SingularAttribute<AgencyServiceRequest_, Long> orderId;
	public static volatile SingularAttribute<AgencyServiceRequest_, String> createdBy;
    public static volatile SingularAttribute<AgencyServiceRequest_, String> createdDate;
    public static volatile SingularAttribute<AgencyServiceRequest_, String> updatedBy;
    public static volatile SingularAttribute<AgencyServiceRequest_, String> updatedDate;

    public static final String REQUESTID = "requestId";
    public static final String TOTALVALUE = "totalValue";
    public static final String DESCRIPTION = "description";
    public static final String CUSTOMERACCOUNT = "customerAccount";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String ORDERID = "orderId";
    public static final String CREATEDBY = "createdBy";
    public static final String UPDATEBY = "updatedBy";
    public static final String CREATEDDATE = "createdDate";
    public static final String UPDATEDDATE = "updatedDate";

}
