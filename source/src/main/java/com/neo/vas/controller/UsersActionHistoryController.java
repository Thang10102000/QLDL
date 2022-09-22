package com.neo.vas.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.neo.vas.service.UahService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.domain.UsersActionHistory;
import com.neo.vas.repository.PrivilegesRepository;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.service.impl.UahServiceImpl;

@RestController
public class UsersActionHistoryController {
	@Autowired
	private UahService uahService;
	@Autowired
	private PrivilegesRepository privilegesRepository;
	@Autowired
	private SystemFunctionalRepository sfRepository;

	@GetMapping(value = { "/user-action-history" })
	public ModelAndView listAmkam(Model model) {
		model.addAttribute("systemfunction", sfRepository.findAll());
		model.addAttribute("privileges", privilegesRepository.findAll());
		ModelAndView modelAndView = new ModelAndView("user_action_history");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping("/uah-search")
	public Map<JSONObject, Integer> searchUah(@RequestParam String username, String module, String privilegess,
			String createdFrom, String createdTo,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size) {
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
			if (!createdFrom.isEmpty() && createdFrom != null){
				createFrom = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(createdFrom);
			}
			if (!createdTo.isEmpty() && createdTo != null){
				createTo = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(createdTo);
			}
			if (!username.isEmpty()){
				username = username.toLowerCase();
			}
			Page<UsersActionHistory> pageUah = uahService.searchUahData(username, module, privilegess, createFrom,
					createTo, realPage, size);
			for (UsersActionHistory uss : pageUah) {
				JSONObject js = uss.createJson();
				data.put(js, pageUah.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
}
