package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
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

import com.neo.vas.domain.Services;
import com.neo.vas.service.ServiceInterface;
import com.neo.vas.service.impl.ServicesImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * project_name: demo Created_by: thulv time: 13/05/2021
 */
@Controller
public class ServiceController {
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping(value = "/service")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Xem')")
	public ModelAndView service() {
		ModelAndView modelAndView = new ModelAndView("Services/service");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping("/new-service")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Thêm')")
	public String newService() {
		return "Services/new_service";
	}

	@PostMapping("/new")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Thêm')")
	public String createService(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
//		RedirectView redt = new RedirectView();
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean rs = serviceInterface.createService(jsonObject, principal);
			if(rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/service", ConstantLog.CREATE,
						principal.getName()+" create service success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return "redirect:/service";
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return "redirect:/service";
			}
		} catch (Exception e) {
			return "redirect:/service";
		}
	}

	@GetMapping(value = "/edit-service/{id}")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Sửa')")
	public String editService(@PathVariable(value = "id") long id, Model model) {
		Services services = serviceInterface.getServicesById(id);
		model.addAttribute("services", services);
		return "Services/edit_service";
	}

	@PostMapping(value = "/update")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Sửa')")
	public ModelAndView editBrandGroup(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean rs = serviceInterface.editService(jsonObject, principal);
			if(rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/service", ConstantLog.EDIT,
						principal.getName()+" edit service "+ jsonObject.getLong("id") +" success");
				reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/service");
			}else {
				reDir.addFlashAttribute("message",ConstantNotify.FAILED);
				return new ModelAndView("redirect:/service");
			}
		} catch (Exception e) {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/service");
		}
	}

	@GetMapping(value = "/delete-service/{id}")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý dịch vụ:Xoá')")
	public ModelAndView deleteService(@PathVariable(value = "id") long id, Principal principal,RedirectAttributes reDir ){
		try {
			this.serviceInterface.deleteServices(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/service", ConstantLog.DELETE,
					principal.getName()+" delete service "+ id +" success");
			reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/service");
		} catch (Exception e) {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/service");
		}
	}
}
