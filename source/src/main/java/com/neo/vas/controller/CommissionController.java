package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.*;
import com.neo.vas.repository.*;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.CommissionService;
import com.neo.vas.service.DiscountPolicyService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.LimitDiscountService;
import com.neo.vas.service.ServiceInterface;
import com.neo.vas.service.impl.AgencyAreaServiceImpl;
import com.neo.vas.service.impl.DiscountPolicyServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.neo.vas.service.impl.ServicesImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: demo Created_by: thulv time: 17/05/2021
 */
@Controller
public class CommissionController {
	@Autowired
	private CommissionService commissionService;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private DiscountPolicyService discountPolicyService;
	@Autowired
	private LimitDiscountService limitDiscountService;
	@Autowired
	private BrandGroupRepository brandGroupRepository;



	@GetMapping(value = "/commission")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Xem')")
	public ModelAndView homeCommision() {
		ModelAndView modelAndView = new ModelAndView("Commissions/commission");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	@GetMapping(value = "/new-commission")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Thêm')")
	public String newCommission(Model model) {
		model.addAttribute("service", serviceInterface.getAllService());
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		return "Commissions/new_commission";
	}

	@PostMapping("/new-commission")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Thêm')")
	public ModelAndView createCommission(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);

//			format data am/kam íd
			String[] partsAm = jsonObject.getString("am-kam").split(Pattern.quote(","));
			JSONArray jsonArrayAm = new JSONArray();
			if (partsAm[0] != "") {
				for (String s : partsAm) {
					jsonArrayAm.put(s);
				}
			}
			jsonObject.put("am-kam", jsonArrayAm);

//			format data agency id
			String[] paramAgency = jsonObject.getString("agency").split(Pattern.quote(","));
			JSONArray jsonAgency = new JSONArray();
			if (paramAgency[0] != "") {
				for (String ag: paramAgency){
					jsonAgency.put(ag);
				}
			}
			jsonObject.put("agency", jsonAgency);

//			format data brand id
			String[] partsBrand = jsonObject.getString("brand").split(Pattern.quote(","));
			JSONArray jsonArrayBrand = new JSONArray();
			if (partsBrand[0] != "") {
				for (String b : partsBrand) {
					jsonArrayBrand.put(b);
				}
			}
			jsonObject.put("brand", jsonArrayBrand);

//			check trùng
			if (!jsonObject.getString("startDate").isEmpty() && !jsonObject.getString("endDate").isEmpty()) {
				Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonObject.getString("startDate"));
				Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonObject.getString("endDate"));
				DiscountCommission checkIsDefault = discountPolicyService.getPolicyIsDefault(startDate, endDate,0);
				if(!jsonObject.getString("checkDefault").isEmpty() && checkIsDefault != null ){
					reDir.addFlashAttribute("error","Chính sách mặc định tại thời điểm hiện tại đã có");
					return new ModelAndView("redirect:/commission");
				}
			}

			boolean rs = commissionService.createCommission(jsonObject,principal);
			if (rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.CREATE,
						principal.getName()+" create commission policy success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/commission");
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return new ModelAndView("redirect:/commission");
			}
		} catch (Exception e) {
			System.err.println(e);
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/commission");
		}
	}

	@GetMapping("/edit-commission/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Sửa')")
	public String editCommission(@PathVariable(value = "id") Long id, Model model) {
		model.addAttribute("service", serviceInterface.getAllService());
		DiscountCommission commisionPolicy = commissionService.getCommissionById(id);
		model.addAttribute("commission", commisionPolicy);
		return "Commissions/edit_commission";
	}

	@PostMapping("/edit-commission")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Sửa')")
	public ModelAndView updateCommission(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			System.out.println(jsonObject);
			boolean rs = commissionService.updateCommission(jsonObject,principal);
			if(rs){
				insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.EDIT,
						principal.getName()+" edit commission policy "+jsonObject.getString("id")+" success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return new ModelAndView("redirect:/commission");
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return new ModelAndView("redirect:/commission");
			}
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/commission");
		}
	}

	@GetMapping("/delete-commission/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hoa hồng:Xoá')")
	public ModelAndView deleteCommission(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir){
		try {
			DiscountCommission coP = commissionService.getCommissionById(id);
			Date date = new Date();
			if(coP.getStartDate().before(date) && coP.getEndDate().after(date)){
				reDir.addFlashAttribute("errDelete", "Chính sách hoa hồng đang còn hiệu lực");
				return new ModelAndView("redirect:/commission");
			}
			this.commissionService.deleteCommission(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/commission", ConstantLog.DELETE,
					principal.getName()+" delete commission policy "+id+" success");
			reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/commission");
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/commission");
		}

	}

	// get list data brandG, serviece by list id
	@GetMapping(value = "/brand-group-list")
	@ResponseBody
	public List<BrandGroup> listBrandGroup(Model model, @RequestParam String idService) {
		List<BrandGroup> list = commissionService.listBrandG(idService);
		model.addAttribute("listBrandG", list);
		return list;
	}

	@GetMapping(value = "/brand-list-name")
	@ResponseBody
	public List<Brand> listBrandByBGName(Model model, @RequestParam String nameBrand) {
		BrandGroup bra = brandGroupRepository.findBrandGroupByGroupName(nameBrand);
		System.out.println(bra.toString());
		List<Brand> listBrand = commissionService.listBrand(bra.getId() + "");
		model.addAttribute("brandList", listBrand);
		System.out.println(listBrand);
		return listBrand;
	}

	@GetMapping(value = "/brand-list")
	@ResponseBody
	public List<Brand> listBrand(Model model, @RequestParam String idBrand) {

		List<Brand> listBrand = commissionService.listBrand(idBrand);
		model.addAttribute("brandList", listBrand);
		return listBrand;
	}

	@GetMapping(value = "/amkam-list")
//	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Xem')")
	@ResponseBody
	public List<Agency> listAmKam(Model model, @RequestParam String idAmKam) {
		List<Agency> listAmKam = commissionService.listAmKam(idAmKam);
		model.addAttribute("brandList", listAmKam);
		return listAmKam;
	}



	//get rate for default rate brand
	@GetMapping(value = "/default-commissionRate")
	@ResponseBody
	public List<DiscountCommission> apiSupport() {
		Date now = new Date();
		System.out.println(now);
		return discountPolicyService.findCommissionEffect(now);
	}
}
