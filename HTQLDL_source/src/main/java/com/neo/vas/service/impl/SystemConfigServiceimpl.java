package com.neo.vas.service.impl;

/**
 * 
 */

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neo.vas.domain.SystemConfig;
import com.neo.vas.repository.SystemConfigRepository;
import com.neo.vas.service.SystemConfigService;

/**
 * @author KhanhBQ
 *
 */
@Service
public class SystemConfigServiceimpl implements SystemConfigService {
	@Autowired
	private SystemConfigRepository scRepo;

	@Override
	@Transactional(readOnly = true)
	public List<SystemConfig> getAllConfigs() {
		return scRepo.findAll();
	}

	@Override
	@Transactional
	public boolean saveConfig(JSONObject data) {
		System.out.println(data);
		SystemConfig newSC = new SystemConfig();
		try {
			if(!scRepo.findAll().isEmpty()) {
				newSC = scRepo.findAll().get(0);
			}
			newSC.setMaxLogin(data.getInt("maxLogin"));
			newSC.setMaxLoginFail(data.getInt("maxLoginFail"));
			newSC.setMinPwLength(data.getInt("minPwLength"));
			newSC.setPwExpiredDate(data.getInt("pwExpiredDate"));
		} catch (Exception e) {
			System.out.println(e);
		}
		return scRepo.saveAndFlush(newSC) != null;
	}
}
