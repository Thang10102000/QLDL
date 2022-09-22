package com.neo.vas.repository;

import com.neo.vas.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long>, JpaSpecificationExecutor<School> {
    School findSchoolsById(Long id);
    School findSchoolBySchoolCode(String schoolCode);
}
