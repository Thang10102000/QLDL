package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 14/05/2021
 */
@StaticMetamodel(Services.class)
public abstract class Service_ {
    public static volatile SingularAttribute<Services, String> serviceName;
    public static volatile SingularAttribute<Services, String> shortCode;
    public static volatile SingularAttribute<Services, String> status;
    public static volatile SingularAttribute<Services, String> serviceId;

    public static final String SERVICENAME = "serviceName";
    public static final String SHORTCODE = "shortCode";
    public static final String STATUS = "status";
    public static final String SERVICEID = "serviceId";
}
