package com.neo.vas.service.impl;

import java.security.Principal;
import java.util.*;

import com.neo.vas.domain.AuthorizedPerson;
import com.neo.vas.repository.AgencyAreaRepository;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.AuthorizedPersonRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.neo.vas.domain.AMKAM;
import com.neo.vas.domain.Agency;
import com.neo.vas.repository.AmkamRepository;
import com.neo.vas.service.AmkamService;
import com.neo.vas.util.VNCharacterUtils;

/**
 * author: YNN
 */
@Service
public class AmkamServiceImpl implements AmkamService {
	@Autowired
	private AmkamRepository amkamRepository;
	private AMKAM amkam;

	@Autowired
	private AgencyRepository agencyRepository;

	@Autowired
	private AgencyAreaRepository agencyAreaRepository;

	@Autowired
	private AuthorizedPersonRepository authorizedPersonRepository;

	@Override
	public List<AMKAM> exportAmkam(String agencyCode, String agencyName, String areaId) throws Exception {
		if (agencyCode != null || agencyName != null || areaId != null) {
			List<Agency> agencys = amkamRepository.findAmkamEx(agencyCode.toUpperCase(),
					VNCharacterUtils.removeAccent(agencyName.toUpperCase()), areaId);
			List<AMKAM> amkams = new ArrayList<AMKAM>();
			for (Agency agency : agencys) {
				amkam = AMKAM.getInstance();
				amkam.setAmkamCode(agency.getAgencyCode());
				amkam.setAmkamName(agency.getAgencyName());
				amkam.setAreaId(agency.getAreaId().getAreaId());
				amkam.setAreaName(agency.getAreaId().getAreaName());
				amkams.add(amkam);
			}
			return amkams;
		} else {
			List<Agency> agencys = amkamRepository.findAll();
			List<AMKAM> amkams = new ArrayList<AMKAM>();
			for (Agency agency : agencys) {
				amkam = AMKAM.getInstance();
				amkam.setAmkamCode(agency.getAgencyCode());
				amkam.setAmkamName(agency.getAgencyName());
				amkam.setAreaId(agency.getAreaId().getAreaId());
				amkam.setAreaName(agency.getAreaId().getAreaName());
				amkams.add(amkam);
			}
			return amkams;
		}
	}

	@Override
	public Page<Agency> searchAmkam(String agencyCode, String agencyName, String areaId, String shopCode, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		if ((agencyName != null && !agencyName.equals("")) || (areaId != null && !areaId.equals("")) || (agencyCode != null && !agencyCode.equals("")) || (shopCode != null && !shopCode.equals(""))) {
			System.out.println(shopCode.isEmpty());
			if(shopCode == null || shopCode.isEmpty() || shopCode.equals("")) {
				return amkamRepository.findAmkam(agencyCode.toUpperCase(),
						VNCharacterUtils.removeAccent(agencyName.toUpperCase()), areaId, pageable);
			}
			return amkamRepository.findAllAmkam(agencyCode.toUpperCase(),
					VNCharacterUtils.removeAccent(agencyName.toUpperCase()), areaId, VNCharacterUtils.removeAccent(shopCode.toUpperCase()), pageable);
		}
		return amkamRepository.findAll(pageable);
	}

	@Override
	public boolean createAmkam(JSONObject data, Principal principal) {
		Agency agency = new Agency();
		try {
			if (!data.getString("amkamName").isEmpty()) {
				agency.setAgencyName(data.getString("amkamName").trim());
			}
			if (!data.getString("areaId").isEmpty()) {
				agency.setAreaId(agencyAreaRepository.getOne(Long.parseLong(data.getString("areaId"))));
			}
			agency.setAgencyType(1);
			if (!data.getString("amkamCode").isEmpty()) {
				agency.setAgencyCode(data.getString("amkamCode").trim());
			}
			if (!data.getString("shopCode").isEmpty()) {
				agency.setShopCode(data.getString("shopCode").trim());
			}
			agency.setType(1);
			agency.setCreatedDate(new Date());
			agencyRepository.saveAndFlush(agency);
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	@Override
	public void deleteAmkam(Long id) {
		List<AuthorizedPerson> lstAuth = authorizedPersonRepository.getAuthorizedPerson(id);
		this.authorizedPersonRepository.deleteInBatch(lstAuth);
		this.agencyRepository.deleteById(id);
	}

	@Override
	public boolean updateAmKam(JSONObject data, Principal principal) {
		Agency agency = new Agency();
		try {
			if (!data.getString("id").isEmpty()) {
				try {
					agency = agencyRepository.findById(Long.parseLong(data.getString("id")));
				} catch (Exception e) {
					return false;
				}
			} else {
				return false;
			}
			if (!data.getString("agencyCode").isEmpty()) {
				agency.setAgencyCode(data.getString("agencyCode").trim());
			}
			if (!data.getString("agencyName").isEmpty()) {
				agency.setAgencyName(data.getString("agencyName").trim());
			}
			if (!data.getString("areaId").isEmpty()) {
				agency.setAreaId(agencyAreaRepository.findAgencyAreaByAreaId(Long.parseLong(data.getString("areaId").trim())));
			}
			if (!data.getString("shopCode").isEmpty()) {
				agency.setShopCode(data.getString("shopCode").trim());
			}
			else {
				agency.setShopCode("");
			}
			agency.setUpdateBy(principal.getName());
			agency.setUpdateDate(new Date());
			agencyRepository.saveAndFlush(agency);
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
