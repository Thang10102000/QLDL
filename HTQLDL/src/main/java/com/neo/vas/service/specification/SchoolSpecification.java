package com.neo.vas.service.specification;

import com.neo.vas.domain.School;
import com.neo.vas.domain.School_;
import org.springframework.data.jpa.domain.Specification;

public final class SchoolSpecification {
    public static Specification<School> hasSchoolName(String schoolName){
        return (root, query, cb) -> cb.like(cb.lower(root.get(School_.SCHOOLNAME)).as(String.class),
                "%" + schoolName.toLowerCase() + "%");
    }
    public static Specification<School> hasSchoolCode(String schoolCode){
        return (root, query, cb) -> cb.like(cb.lower(root.get(School_.SCHOOLCODE)).as(String.class), "%" + schoolCode.toLowerCase() + "%");
    }
    public static Specification<School> hasSchoolType(String schoolType){
        return (root, query, cb) -> cb.like(cb.lower(root.get(School_.SCHOOLTYPE)).as(String.class),"%" + schoolType.toLowerCase() + "%");
    }
    public static Specification<School> hasDescription(String description){
        return (root, query, cb) -> cb.like(root.get(School_.DESCRIPTION).as(String.class),"%" + description + "%");
    }
}
