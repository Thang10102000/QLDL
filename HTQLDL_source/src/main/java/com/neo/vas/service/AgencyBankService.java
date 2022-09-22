package com.neo.vas.service;

import java.util.List;

import com.neo.vas.domain.AgencyBankAccount;
import com.neo.vas.domain.BankAccount;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
public interface AgencyBankService {
    List<AgencyBankAccount> getAllBankAccount();
    BankAccount getAgencyBankAccount(Long id);
}
