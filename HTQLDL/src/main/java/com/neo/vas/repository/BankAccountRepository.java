package com.neo.vas.repository;

import com.neo.vas.domain.AgencyBankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.neo.vas.domain.BankAccount;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    BankAccount findById(long id);
    @Query("select ab from AgencyBankAccount ab where (?1 is null or ab.agencyId = ?1)")
    Page<AgencyBankAccount> findAgencyBankAccount(String agencyId, Pageable pageable);
}
