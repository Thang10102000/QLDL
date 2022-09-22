/**
 * 
 */
package com.neo.vas.controller;

import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.GroupService;
import com.neo.vas.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.neo.vas.repository.AgencyAreaRepository;
import com.neo.vas.service.AgencyService;
import com.neo.vas.service.impl.GroupServiceImpl;
import com.neo.vas.service.impl.UsersServiceImpl;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@ControllerAdvice
public class AdviceController {
	@Autowired
	private GroupService groupService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private AgencyAreaService agencyAreaService;

	@Autowired
	AgencyService agencyService;
	

	@ModelAttribute
	public void addGroup(Model model) {
		model.addAttribute("myGroup", groupService.getAll());
	}

	@ModelAttribute
	public void first5User(Model model) {
		model.addAttribute("my5Users", usersService.loadUsers("", 0, 5));
	}
	
	@ModelAttribute
	public void addUser(Model model) {
		model.addAttribute("myUsers", usersService.getAll());
	}
	
	@ModelAttribute
	public void addArea(Model model) {
		model.addAttribute("myArea", agencyAreaService.getAllAgencyArea());
	}
	
	@ModelAttribute
	public void addAgency(Model model) {
		model.addAttribute("myAgency", agencyService.getAllAgency());
	}
}
