package com.neo.vas.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.service.AuthorizedPersonService;
import com.neo.vas.service.InsertLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.vas.domain.AuthorizedPerson;
import com.neo.vas.service.impl.AuthorizedPersonServiceImpl;

/**
 * project_name: vasonline2021 Created_by: thulv time: 11/06/2021
 */
@RestController
public class AuthorizedPersonRestController {

	@Autowired
	private AuthorizedPersonService authorizedPersonService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/authorized-search")
	@PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Xem')")
	public Map<JSONObject, Integer> searchAuthorized(@RequestParam String agencyName1,
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
			insertLogService.insertLog(principal.getName(),"/vasonline/authorized", ConstantLog.SEARCH,
					principal.getName()+" search authorized person");
			Page<AuthorizedPerson> AuthPage = authorizedPersonService.searchAuthor(agencyName1, realPage, size);
			for (AuthorizedPerson au : AuthPage) {
				JSONObject json = au.createJson();
				data.put(json, AuthPage.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
}
