package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

import com.neo.vas.domain.*;
import com.neo.vas.service.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.repository.*;
import com.neo.vas.service.impl.AsrServiceImpl;

/**
 * @author YNN
 *
 */
@Controller
public class AsrController {
	@Autowired
	private AsrService asrService;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private ServiceInterface serviceInterface;
	@Autowired
	private BrandGroupService brandGroupService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private UsersRepository usersRepository;

	@GetMapping(value = { "/asr" })
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Xem')")
	public ModelAndView listAsr(Model model, Principal principal) throws SQLException {
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")){
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else{
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		model.addAttribute("services", serviceInterface.getAllService());
		return new ModelAndView("AgencyServiceRequest/agency_srv_req");
	}

	@GetMapping("/asr/create")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Thêm')")
	public ModelAndView getAllAsr(Model model) {
		model.addAttribute("services", serviceInterface.getAllService());
		return new ModelAndView("AgencyServiceRequest/asr_create");
	}

	@PostMapping("/asr/create")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Thêm')")
	public String doCreateAsr(@RequestParam Map<String, String> reqParam, Principal principal, Model model,
			RedirectAttributes redir)
			throws SQLException {
		model.addAttribute("services", serviceInterface.getAllService());
		ModelAndView mav = new ModelAndView("AgencyServiceRequest/asr_create");
		try {
			JSONObject reqParamObj = new JSONObject(reqParam);
			mav.addObject(model);
			String res = asrService.saveNewAsr(reqParamObj, principal);
			if (res.equals("INVALID_PARAMS")) {
				redir.addFlashAttribute("message", "INVALID_PARAMS");
				return "redirect:/asr";
			}
			else if (res.equals("PERMISSION_DENIED")) {
				redir.addFlashAttribute("message", "PERMISSION_DENIED");
				return "redirect:/asr";
			}
			else if (res.equals("INVALID_AGENCY")) {
				redir.addFlashAttribute("message", "INVALID_AGENCY");
				return "redirect:/asr";
			}
			else if (res.equals("SUCCESS")) {
				insertLogService.insertLog(principal.getName(), "/vasonline/asr", ConstantLog.CREATE,
						principal.getName() + " create agency service request");
				redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return "redirect:/asr";
			} else {
				redir.addFlashAttribute("message", ConstantNotify.FAILED);
				return "redirect:/asr";
			}
		} catch (Exception e) {
			System.err.println(e);
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}

	@GetMapping("/asr/edit/{requestId}")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Sửa')")
	public ModelAndView getAsrEdit(@PathVariable("requestId") long requestId, Model model, Principal principal) {
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("agencys", agencyService.getAll());
		model.addAttribute("services", serviceInterface.getAllService());
		AgencyServiceRequest asr = asrService.getAsrById(requestId);
		List<Services> sList = new ArrayList<Services>();
		sList.add(asr.getBrandASR().getBrandGroupB().getServicesBG());
		List<BrandGroup> brList = brandGroupService.getListBGByListService(sList);
		List<BrandGroup> asrBgList = new ArrayList<BrandGroup>();
		asrBgList.add(asr.getBrandASR().getBrandGroupB());
//		List<Brand> bList = brandRepository.findByBrandGroupBIn(asrBgList);
		List<Brand> bList = brandService.getListBrandByListBG(asrBgList);
		model.addAttribute("packageGroups", brList);
		model.addAttribute("packages", bList);
		Long levelId = asr.getBrandASR().getApprovedBy();
		model.addAttribute("brLevel", levelId);
			
		return new ModelAndView("AgencyServiceRequest/asr_edit", "asrData", asrService.getAsrById(requestId));
	}

	@PostMapping("/asr/edit")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Sửa')")
	public String doEditUser(@RequestParam Map<String, String> reqParam, Principal principal,
			RedirectAttributes redir) {
		try {
			JSONObject reqParamObj = new JSONObject(reqParam);
			String res = asrService.EditAsr(reqParamObj, principal);
			if (res.equals("INVALID_PARAMS")) {
				redir.addFlashAttribute("message", "INVALID_PARAMS");
				return "redirect:/asr";
			}
			else if (res.equals("PERMISSION_DENIED")) {
				redir.addFlashAttribute("message", "PERMISSION_DENIED");
				return "redirect:/asr";
			}
			else if (res.equals("SUCCESS")) {
				insertLogService.insertLog(principal.getName(), "/vasonline/asr", ConstantLog.EDIT,
						principal.getName() + " edit agency service request");
				redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return "redirect:/asr";
			} else {
				redir.addFlashAttribute("message", ConstantNotify.FAILED);
				return "redirect:/asr";
			}
		} catch (Exception e) {
			System.err.println(e);
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}

	@GetMapping("/asr/delete/{requestId}")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Xoá')")
	public String doDeleteAsr(@PathVariable("requestId") long requestId, Principal principal,
			RedirectAttributes redir)
			throws SQLException {
		if (asrService.deleteAsr(requestId)) {
			insertLogService.insertLog(principal.getName(), "/vasonline/asr", ConstantLog.DELETE,
					principal.getName() + " delete agency service request");
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/asr";
		} else {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}
	
	@GetMapping("/asr/accept/{requestId}")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Thực thi')")
	public String doAcceptAsr(@PathVariable("requestId") long requestId, Principal principal,
			RedirectAttributes redir) {
		if (asrService.acceptAsr(requestId, principal)) {
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/asr";
		}
		else {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}

	@GetMapping("/asr/approve/{requestId}")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Thực thi')")
	public String doApproveAsr(@PathVariable("requestId") long requestId, Principal principal,
			RedirectAttributes redir) {
		if (asrService.approveAsr(requestId, principal)) {
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/asr";
		}
		else {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}

	@GetMapping("/asr/recharge/{requestId}")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Thực thi')")
	public String doRechargeAsr(@PathVariable("requestId") long requestId, Principal principal,
			RedirectAttributes redir) {
		if (asrService.rechargeAsr(requestId, principal)) {
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/asr";
		}
		else {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}

	@GetMapping("/asr/reprocess/{requestId}")
	@PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ:Thực thi')")
	public String doReProcessAsr(@PathVariable("requestId") long requestId, Principal principal,
			RedirectAttributes redir) {
		if (asrService.approveAsr(requestId, principal)) {
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/asr";
		}
		else {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/asr";
		}
	}

	@GetMapping(value = "/asr/total-value")
	@ResponseBody
	public Long listBrand(Model model, @RequestParam String brandId, Principal principal) {
		Long total = asrService.getTotalValue(Long.parseLong(brandId), principal.getName());
		model.addAttribute("totalValue", total);
		return total;
	}

}
