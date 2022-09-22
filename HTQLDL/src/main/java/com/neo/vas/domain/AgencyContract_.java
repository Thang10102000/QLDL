package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: vasonline2021
 * Created_by: thulv
 * time: 10/06/2021
 */
@StaticMetamodel(AgencyContract.class)
public abstract class AgencyContract_ {
    public static volatile SingularAttribute<AgencyContract, String> contractNo;
    public static volatile SingularAttribute<AgencyContract, String> serviceType;
    public static volatile SingularAttribute<AgencyContract, String> signDate;
    public static volatile SingularAttribute<AgencyContract, String> startDate;
    public static volatile SingularAttribute<AgencyContract, String> endDate;
    public static volatile SingularAttribute<AgencyContract, String> status;
    public static volatile SingularAttribute<AgencyContract, String> agencyAC;

    public static final String CONTRACTNO = "contractNo";
    public static final String SERVICETYPE = "serviceType";
    public static final String SIGNDATE = "signDate";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String STATUS = "status";
    public static final String AGENCYAC = "agencyAC";
}
