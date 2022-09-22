package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(School.class)
public abstract class School_ {

    public static volatile SingularAttribute<School, String> schoolName;
    public static volatile SingularAttribute<School, String> schoolCode;
    public static volatile SingularAttribute<School, String> schoolType;
    public static volatile SingularAttribute<School, String> description;

    public static final String SCHOOLNAME = "schoolName";
    public static final String SCHOOLCODE = "schoolCode";
    public static final String SCHOOLTYPE = "schoolType";
    public static final String DESCRIPTION = "description";
}
