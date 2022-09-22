package com.neo.vas.service;

import java.security.Principal;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.AgencyServiceRequest;

/**
 * author: YNN
 */

public interface AsrService {

	public AgencyServiceRequest getAsrById(Long id);

	public Page<AgencyServiceRequest> getAllAsr();

	public boolean deleteAsr(long requestId);

	public String saveNewAsr(JSONObject data, Principal principal);

	public String EditAsr(JSONObject data, Principal principal);
	
	public boolean acceptAsr(long requestId, Principal principal);

	public boolean approveAsr(long requestId, Principal principal);

	boolean rechargeAsr(long requestId, Principal principal);

	Long getTotalValue(Long brandId, String userName);

	public Page<AgencyServiceRequest> searchAsrs(String agency, String packages, String asr_id, String status,
			Date createFrom, Date createTo, Date updateFrom, Date updateTo, int page, int pageSize);
}
