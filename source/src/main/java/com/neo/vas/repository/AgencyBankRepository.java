package com.neo.vas.repository;

import com.neo.vas.domain.AgencyBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Repository
public interface AgencyBankRepository extends JpaRepository<AgencyBankAccount,Long> {
    AgencyBankAccount findAgencyBankAccountById(Long id);
}
