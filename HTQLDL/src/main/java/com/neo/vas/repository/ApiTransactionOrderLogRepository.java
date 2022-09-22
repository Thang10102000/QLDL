package com.neo.vas.repository;

/*
 ** @project_name: vasonline
 ** @author: ThuLv
 ** @created_date: 12/21/2021
 */

import com.neo.vas.domain.ApiTransactionOrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiTransactionOrderLogRepository extends JpaRepository<ApiTransactionOrderLog,Long> {
}
