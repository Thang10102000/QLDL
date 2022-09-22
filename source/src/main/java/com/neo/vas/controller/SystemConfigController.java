/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SystemConfigService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.service.impl.SystemConfigServiceimpl;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class SystemConfigController {
	@Autowired
	private SystemConfigService systemConfigService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/admin/system-config")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý chính sách hệ thống:Xem')")
	public ModelAndView getConfig() {
		ModelAndView mav = new ModelAndView("Systems/system-config");
		mav.addObject("configData", systemConfigService.getAllConfigs().get(0));
		return mav;
	}

	@PostMapping("/admin/system-config")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý chính sách hệ thống:Thêm')")
	public ModelAndView saveConfig(@RequestParam Map<String, String> reqParam, Principal principal) {
		ModelAndView mav = new ModelAndView("Systems/system-config");
		try {
			JSONObject reqParamObj = new JSONObject(reqParam);
			if (reqParamObj.getInt("maxLogin") < 1) {
				reqParamObj.remove("maxLogin");
				reqParamObj.append("maxLogin", 1);
			}
			if (reqParamObj.getInt("maxLoginFail") < 1) {
				reqParamObj.remove("maxLoginFail");
				reqParamObj.append("maxLoginFail", 5);
			}
			if (reqParamObj.getInt("minPwLength") < 1) {
				reqParamObj.remove("minPwLength");
				reqParamObj.append("minPwLength", 1);
			}
			if (reqParamObj.getInt("pwExpiredDate") < 1) {
				reqParamObj.remove("pwExpiredDate");
				reqParamObj.append("pwExpiredDate", 1);
			}
			if (systemConfigService.saveConfig(reqParamObj)) {
				insertLogService.insertLog(principal.getName(),"/vasonline/admin/system-config", ConstantLog.EDIT,
						principal.getName()+" save config system success");
				mav.addObject("notification", "Lưu dữ liệu cấu hình hệ thống thành công");
				mav.addObject("configData", systemConfigService.getAllConfigs().get(0));
			} else {
				mav.addObject("notification", "Lưu dữ liệu cấu hình hệ thống thất bại");
			}
			return mav;
		} catch (Exception e) {
			System.err.println(e);
			return mav;
		}
	}
}
