/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SmsTemplateService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.vas.domain.SmsTemplate;

/**
 * @author KhanhBQ
 *
 */
@RestController
public class RestSmsController {
	@Autowired
	private SmsTemplateService smsTemplateService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/admin/sms/search")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý mẫu tin nhắn:Xem')")
	public Map<JSONObject, Integer> searchSms(@RequestParam String shortcode, @RequestParam String status,
											  @RequestParam String startdate, @RequestParam String enddate,
											  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
											  @RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 10;
		}
		HashMap<JSONObject, Integer> data = new HashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/admin/sms-management", ConstantLog.SEARCH,
					principal.getName()+" search SMS");
			SmsTemplate smsTemplate = new SmsTemplate();
			if (!shortcode.isEmpty()) {
				smsTemplate.setTempCode(shortcode);
			} else {
				smsTemplate.setTempCode("");
			}
			if (!status.isEmpty()) {
				smsTemplate.setStatus(Integer.parseInt(status));
			} else {
				smsTemplate.setStatus(-1);
			}
			if (!startdate.isEmpty()) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startdate);
				smsTemplate.setStartDate(start);
			} else {
				smsTemplate.setStartDate(null);
			}
			if (!enddate.isEmpty()) {
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(enddate);
				smsTemplate.setEndDate(end);
			} else {
				smsTemplate.setEndDate(null);
			}

			Page<SmsTemplate> pages = smsTemplateService.searchSmsTemplate(smsTemplate, realPage, size);
			for (SmsTemplate sms : pages) {
				JSONObject smsObj = sms.createJson();
				data.put(smsObj, pages.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return data;
		}
	}
}
