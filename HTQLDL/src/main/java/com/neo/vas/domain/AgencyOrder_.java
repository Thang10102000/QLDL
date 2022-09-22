package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 05/06/2021
 */
@StaticMetamodel(AgencyOrders.class)
public abstract class AgencyOrder_ {
    public static volatile SingularAttribute<AgencyOrders, String> orderId;
    public static volatile SingularAttribute<AgencyOrders, String> agencyAO;
    public static volatile SingularAttribute<AgencyOrders, String> orderStatusAO;
    public static volatile SingularAttribute<AgencyOrders, String> startDate;
    public static volatile SingularAttribute<AgencyOrders, String> endDate;

    public static final String ORDERID = "orderId";
    public static final String AGENCYAO = "agencyAO";
    public static final String ORDERSTATUSAO = "orderStatusAO";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String PAYMENTMETHOD = "paymentMethod";
}
