package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.repository.DiscountCommissionRepository;
import com.neo.vas.service.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.repository.AgencyAreaRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: vasonline2021 Created_by: thulv time: 12/06/2021
 */
@Controller
public class AgencyDiscountController {
	@Autowired
	private AgencyDiscountService agencyDiscountService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private AgencyAreaService agencyAreaService;
//	@Autowired
//	private DiscountCommissionRepository dcRepo;
	@Autowired
	private CommissionService commissionService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/agency-discount/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Xem')")
	public ModelAndView searchAgencyDiscount(@PathVariable(value = "id") Long id,Model model) {
//		model.addAttribute("agency", agencyRepository.findAgencyByType(0));
		model.addAttribute("agency", agencyService.getLIstAgencyByType(0));
		model.addAttribute("policyId",id);
//		model.addAttribute("discount", dcRepo.findByType(1));
		model.addAttribute("discount", commissionService.getListDCByType(1));
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		ModelAndView mv = new ModelAndView("Discount/detail_discount");
		mv.addObject("page", 1);
		mv.addObject("size", 10);
		return mv;
	}

	@GetMapping("/new-agency-discount")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Thêm')")
	public ModelAndView newAgencyDiscount(Model model) {
//		model.addAttribute("discount", dcRepo.findByType(1));
		model.addAttribute("discount", commissionService.getListDCByType(1));
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		return new ModelAndView("Discount/new_agency_discount");
	}

	@GetMapping("/delete-agency-discount/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Xoá')")
	public ResponseEntity<String> deleteAgencyDiscount(@PathVariable(value = "id") long id, Principal principal){
		try {
			this.agencyDiscountService.deleteAgencyDiscount(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/discount", ConstantLog.DELETE ,
					principal.getName() + "delete agency of discount policy success");
			return new ResponseEntity<>(ConstantNotify.SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ConstantNotify.FAILED, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/new-agency-discount")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Thêm')")
	public ModelAndView createAgencyDiscount(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			String[] parts = jsonObject.getString("agency").split(Pattern.quote(","));
			JSONArray jsonArray = new JSONArray();
			if (parts[0] != "") {
				for (String s : parts) {
					jsonArray.put(s);
				}
			}
			jsonObject.put("agency", jsonArray);
			boolean result = agencyDiscountService.createAgencyDiscount(jsonObject, principal);
			if (result){
				insertLogService.insertLog(principal.getName(),"/vasonline/discount", ConstantLog.CREATE ,
						principal.getName() + "create agency of discount policy success");
				reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/discount");
			}else {
				reDir.addFlashAttribute("message",ConstantNotify.FAILED);
				return new ModelAndView("redirect:/discount");
			}
		} catch (Exception e) {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/discount");
		}
	}
}
