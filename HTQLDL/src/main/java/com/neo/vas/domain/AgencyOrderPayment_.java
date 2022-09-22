package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 09/06/2021
 */
@StaticMetamodel(AgencyOrderPayment.class)
public abstract class AgencyOrderPayment_ {
    public static volatile SingularAttribute<AgencyOrderPayment, String> paymentMethod;
    public static volatile SingularAttribute<AgencyOrderPayment, String> amount;
    public static volatile SingularAttribute<AgencyOrderPayment, String> receiverName;
    public static volatile SingularAttribute<AgencyOrderPayment, String> receiverTime;
    public static volatile SingularAttribute<AgencyOrderPayment, String> agencyOrdersAOP;

    public static final String PAYMENTMETHOD = "paymentMethod";
    public static final String AMOUNT = "amount";
    public static final String RECEIVERNAME = "receiverName";
    public static final String RECEIVERTIME = "receiverTime";
    public static final String AGENCYORDERSAOP = "agencyOrdersAOP";
}
