package com.neo.vas.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.*;
import com.neo.vas.service.AgencyDiscountService;
import com.neo.vas.service.CommissionAmService;
import com.neo.vas.service.InsertLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.BrandRepository;
import com.neo.vas.service.impl.AgencyDiscountServiceImpl;
import com.neo.vas.service.impl.CommissionAmServiceImpl;
import org.springframework.web.bind.annotation.RestController;

/**
 * project_name: vasonline2021 Created_by: thulv time: 12/06/2021
 */
@RestController
public class AgencyDCRestController {
	@Autowired
	private AgencyDiscountService agencyDiscountService;

	@Autowired
	private CommissionAmService commissionAmService;

	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/search-agency-discount")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Xem')")
	public Map<JSONObject, Integer> searchAgDi(@RequestParam String agencyName, String policyName1,
											   @RequestParam(name = "page", required = false, defaultValue = "1") int page,
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
			insertLogService.insertLog(principal.getName(),"/vasonline/discount", ConstantLog.SEARCH,
					principal.getName()+" search agency of discount policy");
			Page<AgencyDiscountCommission> adPage = agencyDiscountService.searchAgencyDiscount(agencyName, policyName1, realPage, size);
			for (AgencyDiscountCommission ad : adPage) {
				JSONObject js = ad.createJson();
				data.put(js, adPage.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("/search-kam-commission")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Xem')")
	public Map<JSONObject, Integer> searchKamCommission(@RequestParam String policyName2, String agencyName,
			@RequestParam(name = "page2", required = false, defaultValue = "1") int page2,
			@RequestParam(name = "size2", required = false, defaultValue = "5") int size2, Principal principal) {
		int realPage = page2 - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size2 < 0) {
			size2 = 5;
		}
		Map<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.SEARCH,
					principal.getName()+" search am/kam, agency of commission policy");
			Page<AgencyDiscountCommission> caPage = commissionAmService.searchKamCommission(policyName2,agencyName, realPage, size2);
			for (AgencyDiscountCommission ca : caPage) {
				JSONObject js = ca.createJson();
				System.err.println(js);
				data.put(js, caPage.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("/search-brand-commission")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Xem')")
	public Map<JSONObject, Integer> searchBrandCommission(@RequestParam String policyName1, String brandName,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		Map<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.SEARCH,
					principal.getName()+" search brand of commission policy");
			Page<BrandCommissionPolicy> bcPage = commissionAmService.searchBrandCommission(policyName1,brandName, realPage, size);
			for (BrandCommissionPolicy bc : bcPage) {
				JSONObject js = bc.createJson();
				data.put(js, bcPage.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
}
