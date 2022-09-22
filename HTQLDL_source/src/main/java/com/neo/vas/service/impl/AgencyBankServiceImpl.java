package com.neo.vas.service.impl;

import com.neo.vas.domain.AgencyBankAccount;
import com.neo.vas.domain.BankAccount;
import com.neo.vas.repository.AgencyBankRepository;
import com.neo.vas.repository.BankAccountRepository;
import com.neo.vas.service.AgencyBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Service
public class AgencyBankServiceImpl implements AgencyBankService {
    @Autowired
    private AgencyBankRepository agencyBankRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Override
    public List<AgencyBankAccount> getAllBankAccount() {
        return agencyBankRepository.findAll();
    }

    @Override
    public BankAccount getAgencyBankAccount(Long id) {
        return bankAccountRepository.findById(id).get();
    }
}
