package com.neo.vas.domain;


import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 15/05/2021
 */
@StaticMetamodel(BrandGroup.class)
public abstract class BrandGroup_ {
    public static volatile SingularAttribute<BrandGroup, String> groupName;
    public static volatile SingularAttribute<BrandGroup, String> status;
    public static volatile SingularAttribute<BrandGroup, String> description;
    public static volatile SingularAttribute<BrandGroup, String> servicesBG;

    public static final String GROUPNAME = "groupName";
    public static final String STATUS = "status";
    public static final String DESCRIPTION = "description";
    public static final String SERVICEBG = "servicesBG";

}
