package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.repository.*;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.AgencyService;
import com.neo.vas.service.BrandService;
import com.neo.vas.service.CommissionAmService;
import com.neo.vas.service.DiscountPolicyService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.ServiceInterface;
import com.neo.vas.service.impl.AgencyAreaServiceImpl;
import com.neo.vas.service.impl.ServicesImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.service.impl.CommissionAmServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: vasonline2021 Created_by: thulv time: 14/06/2021
 */
@Controller
public class CommissionAmController {
	@Autowired
	private CommissionAmService caService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private DiscountPolicyService discountPolicyService;

	@GetMapping("/detail-commission/{id}")
	@PreAuthorize("hasAnyAuthority('Chi tiết chính sách hoa hồng:Xem')")
	@Transactional(readOnly = true)
	public ModelAndView searchDetail(@PathVariable(value = "id") Long id,Model model) {
		model.addAttribute("agency", agencyService.getAll());
		model.addAttribute("brand", brandService.getAllBrand());
		model.addAttribute("commission", discountPolicyService.getCommission());
		model.addAttribute("commissionId",id);
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("service", serviceInterface.getAllService());
		ModelAndView mv = new ModelAndView("Commissions/detail_commission");
		mv.addObject("page2", 1);
		mv.addObject("size2", 5);
		return mv;
	}

	@GetMapping("/delete-kam-commission/{id}")
	@PreAuthorize("hasAnyAuthority('Chi tiết chính sách hoa hồng:Xoá')")
	public ResponseEntity<String> deleteKAMCommission(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir){
		try {
			this.caService.deleteKAMCommission(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.DELETE,
					principal.getName()+" delete am/kam-agency of commission success");

			return new ResponseEntity<>(ConstantNotify.SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ConstantNotify.FAILED, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/delete-brand-commission/{id}")
	@PreAuthorize("hasAnyAuthority('Chi tiết chính sách hoa hồng:Xoá')")
	public ResponseEntity<String> deleteBrandCommission(@PathVariable(value = "id") long id, Principal principal){
		try {
			this.caService.deleteBrandCommission(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.DELETE,
					principal.getName()+" delete brand of commission success");
			return new ResponseEntity<>(ConstantNotify.SUCCESS, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(ConstantNotify.FAILED, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/new-detail-commission")
	@PreAuthorize("hasAnyAuthority('Chi tiết chính sách hoa hồng:Thêm')")
	public ModelAndView newDetail(Model model) {
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("service", serviceInterface.getAllService());
		model.addAttribute("commission", discountPolicyService.getAll());
		return new ModelAndView("Commissions/new_detail_commission");
	}

	@PostMapping("/new-detail-commission")
	@PreAuthorize("hasAnyAuthority('Chi tiết chính sách hoa hồng:Thêm')")
	public ModelAndView createDetailCommission(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			System.err.println("param json: " + jsonObject);

			String[] partsAm = jsonObject.getString("am-kam").split(Pattern.quote(","));
			JSONArray jsonArrayAm = new JSONArray();
			if (partsAm[0] != "") {
				for (String s : partsAm) {
					jsonArrayAm.put(s);
				}
			}
			jsonObject.put("am-kam", jsonArrayAm);
			String[] partsBrand = jsonObject.getString("brand").split(Pattern.quote(","));
			JSONArray jsonArrayBrand = new JSONArray();
			if (partsBrand[0] != "") {
				for (String b : partsBrand) {
					jsonArrayBrand.put(b);
				}
			}
			jsonObject.put("brand", jsonArrayBrand);
			boolean result = caService.createDetailCommission(jsonObject,principal);
			if (result) {
				insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.CREATE,
						principal.getName()+" create am/kam of commission "+jsonObject.getString("policyName3")+" success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/commission");
			} else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return new ModelAndView("redirect:/commission");
			}
		} catch (Exception e) {
			//throw new  RuntimeException(e);
			System.err.println(e);
			return new ModelAndView("redirect:/commission");
		}
	}
}
