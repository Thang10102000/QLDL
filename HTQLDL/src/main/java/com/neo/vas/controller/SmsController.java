/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SmsTemplateService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class SmsController {
	@Autowired
	private SmsTemplateService smsTemplateService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/admin/sms-management")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Xem')")
	public ModelAndView viewSms() {
		return new ModelAndView("SmsTemplate/sms-template");
	}

	@GetMapping("/admin/sms-management/create")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Thêm')")
	public ModelAndView createSms() {
		return new ModelAndView("SmsTemplate/sms-create");
	}

	@GetMapping("/admin/sms-management/edit/{id}")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Sửa')")
	public ModelAndView editSms(@PathVariable("id") String id) {
		return new ModelAndView("SmsTemplate/sms-edit", "smsData", smsTemplateService.getById(id));
	}

	@PostMapping("/admin/sms-management/create")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Thêm')")
	public ModelAndView saveNewSms(@RequestParam Map<String, String> reqParam, Principal principal, RedirectAttributes reDir) throws SQLException {
		JSONObject data = new JSONObject(reqParam);
		if (smsTemplateService.saveNew(data, principal)) {
			String description = principal.getName() + " create sms success";
			insertLogService.insertLog(principal.getName(),"/vasonline/admin/sms-management", ConstantLog.CREATE, description);
			reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/admin/sms-management");
		} else {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/admin/sms-management");
		}
	}

	@PostMapping("/admin/sms-management/edit")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Sửa')")
	public ModelAndView editSms(@RequestParam Map<String, String> reqParam, Principal principal,RedirectAttributes reDir) throws SQLException {
		JSONObject data = new JSONObject(reqParam);
		if (smsTemplateService.editSms(data, principal)) {
			insertLogService.insertLog(principal.getName(),"/vasonline/admin/sms-management", ConstantLog.EDIT,
					principal.getName()+" edit sms success");
			reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/admin/sms-management");
		} else {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/admin/sms-management");
		}
	}
	@GetMapping("/admin/sms-management/delete/{smsCode}")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Xoá')")
	public ModelAndView deleteSms(@PathVariable("smsCode") String id,Principal principal,String description,RedirectAttributes reDir) {
		try {
			this.smsTemplateService.deleteSms(id,principal);
			description = principal.getName() + " delete agency success";
			insertLogService.insertLog(principal.getName(),"/vasonline/admin/sms-management", ConstantLog.DELETE ,description);
			reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/admin/sms-management");
		} catch (Exception e) {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/admin/sms-management");
		}
	}

}
