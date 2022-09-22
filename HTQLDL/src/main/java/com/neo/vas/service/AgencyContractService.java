package com.neo.vas.service;

import com.neo.vas.domain.Deposits;
import com.neo.vas.dto.AgencyContractDTO;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.AgencyContract;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

public interface AgencyContractService {

	public Page<AgencyContract> getAll(String pageNumber, String sortField,
			String sortDir, String contractNo, String agencyName, String status,
			String serviceType,String recordNum);
	boolean createAgencyContract(JSONObject data, Principal principal, MultipartFile[] file);
	public void save(AgencyContract agencyContract);
	public AgencyContract get(Long id);
	public void delete(Long id);
	Page<AgencyContract> getAll(String contractNo, String agencyId, String status, int page, int size);
	Page<AgencyContract> searchAgencyContract(String contractNo, String agencyId, String status, String startDate,String endDate, int page, int size);
	List<AgencyContract> exportFile(HttpServletResponse response , AgencyContract agencyContract);
	boolean editAgengyContract(JSONObject data, Principal principal, MultipartFile[] file);
//	Page<AgencyContractDTO> searchAgencyContractArea(String contractNo, Long id, Integer status, Date startDate, Date endDate, int page, int size);
}
