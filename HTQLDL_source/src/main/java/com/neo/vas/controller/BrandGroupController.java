package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.Brand;
import com.neo.vas.service.InsertLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.domain.BrandGroup;
import com.neo.vas.service.BrandGroupService;
import com.neo.vas.service.ServiceInterface;
import com.neo.vas.service.impl.BrandGroupServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: demo Created_by: thulv time: 14/05/2021
 */
@Controller
public class BrandGroupController {
	@Autowired
	private BrandGroupService brandGroupService;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/brand-group")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm gói cước:Xem')")
	public ModelAndView brandGroup(Model model) {
		model.addAttribute("services", serviceInterface.getAllService());
		ModelAndView modelAndView = new ModelAndView("Services/brand_group");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping(value = "/new-brand-group")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm gói cước:Thêm')")
	public String newBrandGroup(Model model) {
		model.addAttribute("services", serviceInterface.getAllService());
		return "Services/new_brand_group";
	}

	@PostMapping(value = "/new-brand-group")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm gói cước:Thêm')")
	public ModelAndView createBrandGroup(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean rs = brandGroupService.createdBrandGroup(jsonObject, principal);
			if (rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/brand-group", ConstantLog.CREATE,
						principal.getName()+" create brand group success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/brand-group");
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return new ModelAndView("redirect:/brand-group");
			}
		} catch (Exception e) {
			System.err.println(e);
			return new ModelAndView("redirect:/brand-group");
		}
	}

	@GetMapping(value = "/edit-brand-group/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm gói cước:Sửa')")
	public String editBrandGroup(@PathVariable(value = "id") Long id, Model model) {
		BrandGroup brandGroup = brandGroupService.getBrandGroupById(id);
		model.addAttribute("brandGroupName",brandGroup.getGroupName());
		model.addAttribute("brandGroup", brandGroup);
		model.addAttribute("services", serviceInterface.getAllService());
		return "Services/edit_brand_group";
	}

	@PostMapping(value = "/edit-brand-group")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm gói cước:Sửa')")
	public ModelAndView editBrandGroup(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean rs = brandGroupService.updateBrandGroup(jsonObject, principal);
			if (rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/brand-group", ConstantLog.EDIT,
						principal.getName()+" edit brand group "+ jsonObject.getString("id") +" success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/brand-group");
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return new ModelAndView("redirect:/brand-group");
			}
		} catch (Exception e) {
			System.err.println(e);
			return new ModelAndView("redirect:/brand-group");
		}
	}

	@GetMapping(value = "/delete-brand-group/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm gói cước:Xoá')")
	public ModelAndView deleteBrandGroup(@PathVariable(value = "id") Long id, Principal principal,RedirectAttributes reDir){
		try {
			this.brandGroupService.deleteBrandGroup(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/brand-group", ConstantLog.DELETE,
					principal.getName()+" delete brand group "+ id +" success");
			reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/brand-group");
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/brand-group");
		}
	}
}
