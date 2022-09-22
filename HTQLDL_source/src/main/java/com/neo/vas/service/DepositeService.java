package com.neo.vas.service;

import com.neo.vas.domain.Agency;
import com.neo.vas.domain.AgencyContract;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.Deposits;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Date;


public interface DepositeService {

	Page<Deposits> getAll( String agencyName, String depositsAmount, String startDate, String endDate, String status, int page, int size);
	void save(Deposits deposite);
	boolean createDeposit(JSONObject data, Principal principal, MultipartFile[] file);
	boolean editDeposit(JSONObject data, Principal principal, MultipartFile[] file);
	Deposits get(Long id);
	List<Deposits> exportFile(HttpServletResponse response, String agencyName, String depositsAmount, String startDate, String endDate, String status);
	void delete(Long id);
	Deposits getDepositsExist(Long agencyId, Date startDate , Date endDate);
	List<AgencyContract> getAgencyContractByAgencyId(Long agencyId);
}