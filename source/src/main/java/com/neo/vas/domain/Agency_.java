package com.neo.vas.domain;

import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 27/05/2021
 */
@StaticMetamodel(Agency.class)
public abstract class Agency_ {
    public static final String AGENCYNAME = "agencyName";
    public static final String AREAID = "areaId";
    public static final String AGENCYTYPE = "agencyType";
    public static final String STATUS = "status";
}
