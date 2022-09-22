package com.neo.vas.service;

import com.neo.vas.domain.AgencyBankAccount;
import org.json.JSONObject;

import com.neo.vas.domain.BankAccount;
import org.springframework.data.domain.Page;

import java.security.Principal;

/**
 * project_name: demo Created_by: thulv time: 26/05/2021
 */
public interface BankAccountService {

	Page<AgencyBankAccount> searchBankAccount(String agencyName, int page, int size);
	boolean createBankAccount(JSONObject data, Principal principal);

	boolean updateBankAccount(JSONObject data, Principal principal);

	BankAccount getBankAccountById(Long id);

	void deleteById(Long id);
}
