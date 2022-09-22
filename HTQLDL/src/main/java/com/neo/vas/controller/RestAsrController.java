package com.neo.vas.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.neo.vas.service.AsrService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.AgencyServiceRequest;
import com.neo.vas.service.InsertLogService;

@RestController
public class RestAsrController {
	@Autowired
	private AsrService asrService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/asr/search")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Xem')")
	public Map<JSONObject, Integer> doSearchAsr(@RequestParam String agency, @RequestParam String packages,
			@RequestParam String status, @RequestParam String asr_id, @RequestParam String createdDateFrom,
			@RequestParam String createdDateTo, @RequestParam String updatedDateFrom,
			@RequestParam String updatedDateTo,
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
			Date createFrom = null;
			Date createTo = null;
			Date updateFrom = null;
			Date updateTo = null;
			if (!createdDateFrom.isEmpty() && createdDateFrom != null)
				createFrom = new SimpleDateFormat("dd/MM/yyyy").parse(createdDateFrom);
			if (!createdDateTo.isEmpty() && createdDateTo != null)
				createTo = new SimpleDateFormat("dd/MM/yyyy").parse(createdDateTo);
			if (!updatedDateFrom.isEmpty() && updatedDateFrom != null)
				updateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(updatedDateFrom);
			if (!updatedDateTo.isEmpty() && updatedDateTo != null)
				updateTo = new SimpleDateFormat("dd/MM/yyyy").parse(updatedDateTo);
			Page<AgencyServiceRequest> pages = asrService.searchAsrs(agency, packages, asr_id, status, createFrom,
					createTo, updateFrom, updateTo, realPage, size);
			for (AgencyServiceRequest asrs : pages) {
				JSONObject adata = asrs.createJson();
				data.put(adata, pages.getTotalPages());
			}
			insertLogService.insertLog(principal.getName(), "/vasonline/asr", ConstantLog.SEARCH,
					principal.getName() + " search agency service request");
			return data;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
