package com.neo.vas.repository;

import com.neo.vas.domain.AgencyDCHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/26/2021
 */
@Repository
public interface AgencyDCHistoryRepository extends JpaRepository<AgencyDCHistory,Long> {
}
