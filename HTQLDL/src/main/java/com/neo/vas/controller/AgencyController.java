package com.neo.vas.controller;

import java.security.Principal;
import java.util.*;


import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Users;
import com.neo.vas.repository.AgencyRepository;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.AgencyService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.ProvinceService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.neo.vas.repository.AuthorizedPersonRepository;
import com.neo.vas.service.impl.AgencyServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: demo Created_by: thulv time: 24/05/2021
 */
@Controller
public class AgencyController {
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private AuthorizedPersonRepository authorizedPersonRepository;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private ProvinceService provinceService;

//    Tim kiem
	@GetMapping("/agency")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Xem')")
	public ModelAndView getAllAgency(Model model, Principal principal) {
//		Hiển thị ctkv theo từng account
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")){
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else{
			model.addAttribute("agencyArea", null);
		}
		ModelAndView modelAndView = new ModelAndView("Agency/agency");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 10);
		return modelAndView;
	}

//    them moi

	@GetMapping("/new-agency")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Thêm')")
	public ModelAndView newAgency(Model model,Principal principal) {
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		model.addAttribute("listProvince", provinceService.getAllProvince());
		if(levelName.equals("MDS")){
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else{
			model.addAttribute("agencyArea", null);
		}
		return new ModelAndView("Agency/new_agency");
	}

	@PostMapping("/new-agency")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Thêm')")
	public ModelAndView createAgency(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			String description = "";
			ModelAndView mav = new ModelAndView("redirect:/agency");
			JSONObject jsonObject = new JSONObject(reParam);
			Agency agency = agencyService.getAgencyByCode(jsonObject.getString("agencyCode"));
			if (agency != null){
				reDir.addFlashAttribute("exist",ConstantNotify.FAILED);
				return mav;
			}
			boolean result = agencyService.createAgency(jsonObject,principal);
			if (result){
				description = principal.getName() + " create agency success";
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			}
			insertLogService.insertLog(principal.getName(),"/vasonline/agency", ConstantLog.CREATE, description);
			return mav;
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/agency");
		}
	}

	@GetMapping("/edit-agency/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Sửa')")
	public ModelAndView editAgency(@PathVariable(value = "id") Long id, Model model, Principal principal) {
		ModelAndView mav = new ModelAndView("Agency/edit_agency");
		//		Hiển thị ctkv theo từng account
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")){
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
			model.addAttribute("listProvince", provinceService.getAllProvince());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("agencyArea",null);
			}
		}
		else{
			model.addAttribute("agencyArea", null);
		}
		mav.addObject("editAgency",agencyService.getAgencyById(id));
		mav.addObject("represent",authorizedPersonRepository.getRepresent(id));
		mav.addObject("authorized",authorizedPersonRepository.getAuthorized(id));
		mav.addObject("contact",authorizedPersonRepository.getContact(id));
		return mav;
	}

	@PostMapping("/edit-agency")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Sửa')")
	public ModelAndView updateAgency(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir){
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean result = agencyService.updateAgency(jsonObject, principal);
			String description = "";
			ModelAndView modelAndView= new ModelAndView("redirect:/agency");
			if (result){
				description = principal.getName() + " edit agency success";
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			}
			insertLogService.insertLog(principal.getName(),"/vasonline/agency", ConstantLog.EDIT,description);
			return modelAndView;
		} catch (Exception e) {
			System.err.println("Lỗi >>>> " + e);
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/agency");
		}
	}

	@GetMapping("/delete-agency/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Xoá')")
	public ModelAndView deleteAgency(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir){
		String description = "";
		try {
			this.agencyService.deleteAgency(id);
			description = principal.getName() + " delete agency success";
			insertLogService.insertLog(principal.getName(),"/vasonline/agency", ConstantLog.DELETE ,description);
			reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/agency");
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/agency");                  
		}
	}
//	get areaId by agency Id
	@GetMapping("/get-bank-area-id")
	@ResponseBody
	public Long getAreaId(@RequestParam String agencyId){
		try {
			Agency agency = agencyService.getOne(Long.parseLong(agencyId));
//			Agency agency = agencyRepository.getAgency(Long.parseLong(agencyId));
			long areaId = agency.getAreaId().getAreaId();
			return areaId;
		}catch (Exception e){
			System.err.println(e);;
			return null;
		}
	}

//	get all agency by Area Id
	@GetMapping("/get-all-agency-by-area")
	@ResponseBody
	public List<Agency> getAllAgencyByAreaId(@RequestParam String areaId){
		try {
			if (areaId != null){
				List<Agency> lstAgency = agencyService.getListAgencyByArea(Long.parseLong(areaId));
				return lstAgency;
			}else {
				return null;
			}
		}catch (Exception e){
			System.err.println(e);;
			return null;
		}
	}

	@GetMapping("/get-all-agency")
	@ResponseBody
	public List<Agency> getAllAgency(){
		try {
			return agencyService.getAll();
		}catch (Exception e){
			System.err.println(e);;
			return null;
		}
	}
}
