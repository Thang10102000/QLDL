package com.neo.vas.service;

import java.security.Principal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.AMKAM;
import com.neo.vas.domain.Agency;

/**
 * author: YNN
 */
public interface AmkamService {
//	List<AMKAM> getAllAmkam();
	public List<AMKAM> exportAmkam(String agencyCode, String agencyName, String areaId) throws Exception;

	public Page<Agency> searchAmkam(String agencyCode, String agencyName, String areaId, String shopCode, int page, int size);

	boolean createAmkam(JSONObject data, Principal principal);

	void deleteAmkam(Long id);

	boolean updateAmKam(JSONObject data, Principal principal);

}
