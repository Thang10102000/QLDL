/**
 * 
 */
package com.neo.vas.service;

import java.util.List;

import org.json.JSONObject;

import com.neo.vas.domain.SystemConfig;

/**
 * @author KhanhBQ
 *
 */
public interface SystemConfigService {
	public List<SystemConfig> getAllConfigs();
	public boolean saveConfig(JSONObject data);
}
