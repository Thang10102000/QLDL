package com.neo.vas.domain;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 15/05/2021
 */
@StaticMetamodel(Brand.class)
public abstract class Brand_ {
    public static volatile SingularAttribute<Brand, String> brandId;
    public static volatile SingularAttribute<Brand, String> brandName;
    public static volatile SingularAttribute<Brand, String> status;
    public static volatile SingularAttribute<Brand, String> brandType;

    public static final String BRANDID = "brandId";
    public static final String BRANDNAME = "brandName";
    public static final String STATUS = "status";
    public static final String BRANDTYPE = "brandType";
    public static final String BRANDGROUP = "brandGroupB";

}
