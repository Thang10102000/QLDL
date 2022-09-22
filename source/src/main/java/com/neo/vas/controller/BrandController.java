package com.neo.vas.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.repository.DiscountCommissionRepository;
import com.neo.vas.service.DiscountPolicyService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.LevelsService;
import com.neo.vas.service.ServiceInterface;
import com.neo.vas.service.impl.DiscountPolicyServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.domain.Brand;
import com.neo.vas.service.BrandGroupService;
import com.neo.vas.service.BrandService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: demo Created_by: thulv time: 14/05/2021
 */
@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private BrandGroupService brandGroupService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private BrandGroupService bgService;
	@Autowired
	private LevelsService levelsService;
	@Autowired
	private DiscountPolicyService discountPolicyService;

	@GetMapping(value = "/brand")
	@PreAuthorize("hasAnyAuthority('Quản lý gói cước:Xem')")
	public ModelAndView getBrand(Model model) {
		model.addAttribute("brandGroup", brandGroupService.getAllBrandGroup());
		model.addAttribute("service", serviceInterface.getAllService());
		ModelAndView modelAndView = new ModelAndView("Services/brand");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping(value = "/new-brand")
	@PreAuthorize("hasAnyAuthority('Quản lý gói cước:Thêm')")
	public String newBrand(Model model) {
		model.addAttribute("brandG", brandGroupService.getAllBrandGroup());
		model.addAttribute("service", serviceInterface.getAllService());
		model.addAttribute("levels",levelsService.getMdsCtkv());
		return "Services/new_brand";
	}

	@PostMapping(value = "/new-brand")
	@PreAuthorize("hasAnyAuthority('Quản lý gói cước:Thêm')")
	public String createBrand(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean rs = brandService.createBrand(jsonObject, principal);
			if (rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/brand", ConstantLog.CREATE,
						principal.getName()+" create brand success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return"redirect:/brand";
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return "redirect:/brand";
			}
		} catch (Exception e) {
			System.err.println(e);
			return "redirect:/brand";
		}
	}

	@GetMapping(value = "/edit-brand/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý gói cước:Sửa')")
	public String editBrand(@PathVariable(value = "id") Long id, Model model) {
		Brand brand = brandService.getBrandById(id);
		model.addAttribute("brands", brand);
		model.addAttribute("serviceS", serviceInterface.getServiceByBG(bgService.getBGByBrandId(id).getId()));
		model.addAttribute("service", serviceInterface.getAllService());
		model.addAttribute("brandName", brand.getBrandName());
		model.addAttribute("levels",levelsService.getMdsCtkv());
		if(brand.getDcPolicyId() != null) {
			model.addAttribute("dc", discountPolicyService.getDiscountPolicyById(brand.getDcPolicyId()));
		}
		else {
			model.addAttribute("dc", new DiscountCommission());
		}
//    model.addAttribute("brandG", brandGroupService.getAllBrandGroup());
			return "Services/edit_brand";
	}

	@PostMapping(value = "/edit-brand")
	@PreAuthorize("hasAnyAuthority('Quản lý gói cước:Sửa')")
	public ModelAndView updateBrand(@RequestParam Map<String, String> reParam, Principal principal,RedirectAttributes reDir) {
		try {
			JSONObject jsonParam = new JSONObject(reParam);
			System.out.println(jsonParam);
			boolean rs = brandService.saveBrand(jsonParam, principal);
			if (rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/brand", ConstantLog.EDIT,
						principal.getName()+" edit brand "+ jsonParam.getString("id") +" success");
				reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/brand");
			}else {
				reDir.addFlashAttribute("message",ConstantNotify.FAILED);
				return new ModelAndView("redirect:/brand");
			}
		} catch (Exception e) {
			System.err.println(e);
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/brand");
		}
	}

	@GetMapping(value = "/delete-brand/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý gói cước:Xoá')")
	public ModelAndView deleteBrand(@PathVariable(value = "id") long id, Principal principal,RedirectAttributes reDir){
		try {
			this.brandService.deleteById(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/brand", ConstantLog.DELETE,
					principal.getName()+" delete brand "+ id +" success");
			reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/brand");
		} catch (Exception e) {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/brand");
		}
	}

	@GetMapping(value = "/list-brand-find-by-brandG")
	@ResponseBody
	public List<Brand> listBrand(Model model, @RequestParam String idBrand) {
		List<Brand> listBrand = brandService.listBrand(idBrand);
		model.addAttribute("brandList", listBrand);
		return listBrand;
	}
}