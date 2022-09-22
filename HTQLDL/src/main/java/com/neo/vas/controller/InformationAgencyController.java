package com.neo.vas.controller;

import java.security.Principal;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SystemConfigService;
import com.neo.vas.service.UsersService;
import com.neo.vas.service.impl.UsersServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.impl.SystemConfigServiceimpl;

/**
 * project_name: vasonline2021 Created_by: thulv time: 14/06/2021
 */
@Controller
public class InformationAgencyController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private SystemConfigService scsService;
	@Autowired
	private InsertLogService insertLogService;

	@PostMapping("/information-agency")
	@PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Thêm')")
	public ModelAndView doCreateUser(@RequestParam Map<String, String> reqParam, Principal principal, Model model) {
		ModelAndView mav = new ModelAndView("Agency/information_agency");
		try {
			JSONObject reqParamObj = new JSONObject(reqParam);
			boolean validate = true;
			// check empty username
			if (!reqParamObj.has("username") || reqParamObj.getString("username").isEmpty()) {
				model.addAttribute("usernameValidate", "Tên đăng nhập không được để trống");
				validate = false;
			}
			// check duplicate username
			if (usersService.checkExistById(reqParamObj.getString("username"))) {
				model.addAttribute("usernameValidate", "Tên đăng nhập trùng");
				validate = false;
			}
			// check empty password
			if (!reqParamObj.has("password") || reqParamObj.getString("password").isEmpty()) {
				model.addAttribute("passwordValidate", "Mật khẩu không được để trống");
				validate = false;
			}
			// check system setting password length
			if (reqParamObj.getString("password").length() < scsService.getAllConfigs().get(0).getMinPwLength()) {
				model.addAttribute("passwordValidate",
						"Mật khẩu không được ít hơn " + scsService.getAllConfigs().get(0).getMinPwLength() + " ký tự");
				validate = false;
			}
			// check repassword empty
			if (!reqParamObj.has("re-password") || reqParamObj.getString("re-password").isEmpty()) {
				model.addAttribute("password2Validate", "Mật khẩu không được để trống");
				validate = false;
			}
			// check password == repassword
			if (!reqParamObj.getString("password").equals(reqParamObj.getString("re-password"))) {
				model.addAttribute("password2Validate", "Mật khẩu không trùng khớp");
				validate = false;
			}
			// check fullname empty
			if (!reqParamObj.has("fullname") || reqParamObj.getString("fullname").isEmpty()) {
				model.addAttribute("fullnameValidate", "Tên không được để trống");
				validate = false;
			}
			// check levels empty
			if (!reqParamObj.has("levels") || reqParamObj.getString("levels").isEmpty()) {
				model.addAttribute("levelsValidate", "Hãy chọn cấp tài khoản");
				validate = false;
			}
			// check groups empty
			if (!reqParamObj.has("groups") || reqParamObj.getString("groups").isEmpty()) {
				model.addAttribute("groupsValidate", "Hãy chọn nhóm tài khoản");
				validate = false;
			}
			// check email empty
			if (!reqParamObj.has("email") || reqParamObj.getString("email").isEmpty()) {
				model.addAttribute("emailValidate", "Email không được để trống");
				validate = false;
			}
			// check phone empty
			if (!reqParamObj.has("phone") || reqParamObj.getString("phone").isEmpty()) {
				model.addAttribute("phoneValidate", "Số điện thoại không được để trống");
				validate = false;
			}
			// check fax empty
			if (!reqParamObj.has("fax") || reqParamObj.getString("fax").isEmpty()) {
				model.addAttribute("faxValidate", "Số fax không được để trống");
				validate = false;
			}
			// check address empty
			if (!reqParamObj.has("address") || reqParamObj.getString("address").isEmpty()) {
				model.addAttribute("addressValidate", "Địa chỉ không được để trống");
				validate = false;
			}
			mav.addObject(model);
			if (validate) {
				insertLogService.insertLog(principal.getName(),"/vasonline/agency", ConstantLog.CREATE,
						principal.getName()+" create user agency success");
				model.addAttribute("success", "Tạo mới thành công");
				usersService.saveNewUser(reqParamObj, principal);
			} else {
				return mav;
			}
			return mav;
		} catch (Exception e) {
			model.addAttribute("success", "Lỗi " + e);
			System.err.println(e);
			return mav;
		}
	}

}
