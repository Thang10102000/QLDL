package com.neo.vas.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.repository.DiscountCommissionRepository;
import com.neo.vas.service.CommissionService;
import com.neo.vas.service.DiscountPolicyService;
import com.neo.vas.service.InsertLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.neo.vas.service.impl.DiscountPolicyServiceImpl;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.crypto.Data;

/**
 * project_name: vasonline2021 Created_by: thulv time: 03/06/2021
 */
@RestController
public class CommissionDiscountRestController {
	@Autowired
	private CommissionService commissionService;
	@Autowired
	private DiscountPolicyService discountPolicyService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/commission-search")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Xem')")
	public Map<JSONObject, Integer> searchCommission(@RequestParam String policyName, String isDefault,
													 String startDate, String endDate,
													 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
													 @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		HashMap<JSONObject, Integer> data = new HashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.SEARCH,
					principal.getName()+" search commission policy");
			Page<DiscountCommission> pageCommission = commissionService.searchCommissionData(policyName, isDefault,
					startDate, endDate,0, realPage, size);
			for (DiscountCommission co : pageCommission) {
				JSONObject js = co.createJson();
				data.put(js, pageCommission.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("/discount-search")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Xem')")
	public Map<JSONObject, Integer> searchDiscount(@RequestParam String policyName,String startDate, String isDefault, String endDate,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/discount", ConstantLog.SEARCH,
					principal.getName()+" search discount policy");
			Page<DiscountCommission> pageDiscount = discountPolicyService.searchDiscountPolicy(policyName, isDefault,startDate, endDate,realPage, size);
			for (DiscountCommission dc : pageDiscount) {
				JSONObject js = dc.createJson();
				data.put(js, pageDiscount.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	}

