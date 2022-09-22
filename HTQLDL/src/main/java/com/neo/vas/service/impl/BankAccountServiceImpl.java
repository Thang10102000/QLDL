package com.neo.vas.service.impl;

import com.neo.vas.domain.AgencyBankAccount;
import com.neo.vas.domain.BankAccount;
import com.neo.vas.repository.AgencyBankRepository;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.BankAccountRepository;
import com.neo.vas.service.BankAccountService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private AgencyBankRepository agencyBankRepository;
    Date date = new Date();

    @Override
    public boolean createBankAccount(JSONObject data, Principal principal) {
        BankAccount bankAccount = new BankAccount();
        try {
            if (!data.getString("bankAccountNo").isEmpty()) {
                bankAccount.setBankAccountNo(data.getString("bankAccountNo").trim());
            }
            if (!data.getString("bankName").isEmpty()) {
                bankAccount.setBankName(data.getString("bankName").trim());
            }
            if (!data.getString("bankBranch").isEmpty()) {
                bankAccount.setBankBranch(data.getString("bankBranch").trim());
            }
            if (!data.getString("bankAddress").isEmpty()) {
                bankAccount.setBankAddress(data.getString("bankAddress").trim());
            }
            bankAccount.setCreatedBy(principal.getName());
            bankAccount.setCreatedDate(date);
            BankAccount newBA = bankAccountRepository.saveAndFlush(bankAccount);
            BankAccount bankAccountId = bankAccountRepository.findById(newBA.getId());
            if (!data.getString("agencyName").isEmpty()) {
                AgencyBankAccount agencyBankAccount = new AgencyBankAccount();
                agencyBankAccount.setAgencyId(agencyRepository.getOne(Long.parseLong(data.getString("agencyName").trim())));
                agencyBankAccount.setBankAccountId(bankAccountId);
                agencyBankRepository.saveAndFlush(agencyBankAccount);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateBankAccount(JSONObject data, Principal principal) {
        BankAccount bankAccount = new BankAccount();
        try {
            if (!data.getString("id").isEmpty()) {
                bankAccount = bankAccountRepository.findById(Long.parseLong(data.getString("id")));
            } else {
                return false;
            }
            if (!data.getString("bankAccountNo").isEmpty()) {
                bankAccount.setBankAccountNo(data.getString("bankAccountNo").trim());
            }
            if (!data.getString("bankName").isEmpty()) {
                bankAccount.setBankName(data.getString("bankName").trim());
            }
            if (!data.getString("bankBranch").isEmpty()) {
                bankAccount.setBankBranch(data.getString("bankBranch").trim());
            }
            if (!data.getString("bankAddress").isEmpty()) {
                bankAccount.setBankAddress(data.getString("bankAddress").trim());
            }
            bankAccount.setUpdateBy(principal.getName());
            bankAccount.setUpdateDate(date);
            bankAccountRepository.saveAndFlush(bankAccount);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Page<AgencyBankAccount> searchBankAccount(String agencyId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        if (agencyId != null){
           return bankAccountRepository.findAgencyBankAccount(agencyId,pageable);
        }
        return agencyBankRepository.findAll(pageable);
    }

    @Override
    public BankAccount getBankAccountById(Long id) {
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(id);
        BankAccount bankAccount = null;
        if (bankAccountOpt.isPresent()){
            bankAccount = bankAccountOpt.get();
        }else {
            throw new RuntimeException("Khong tim thay tai khoan " + id);
        }
        return bankAccount;
    }

    @Override
    public void deleteById(Long id) {
        try {
            AgencyBankAccount agencyBankAccount = agencyBankRepository.findAgencyBankAccountById(id);
            this.agencyBankRepository.deleteById(id);
            this.bankAccountRepository.deleteById(agencyBankAccount.getBankAccountId().getId());
        }catch (Exception e){
            System.err.println(e);
        }

    }
}
