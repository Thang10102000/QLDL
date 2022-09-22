package com.neo.vas.controller;

import java.io.IOException;
import java.io.Writer;
import java.security.Principal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.*;
import com.neo.vas.repository.*;
import com.neo.vas.security.UserService;
import com.neo.vas.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * @author DatND
 */
@Controller
@RequestMapping("/agencyContract")
public class AgencyContractsController {

	@Autowired
	private AgencyContractService service;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private AgencyContractRepository agencyContractRepository;
	@Autowired
	private ImgDepositsRepository imgDepositsRepository;

	private static final String LIST_AGENCY = "listAgency";

	// Index page, get all country, display 5 records
	@GetMapping(value = "/index")
	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Xem')")
	public ModelAndView indexPage(Model model, Principal principal) {
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")){
			model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("listArea", agencyAreaService.getAgencyAreaById(areaId));
		}else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("listArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("listArea",null);
			}
		}else{
			model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		}
		ModelAndView modelAndView = new ModelAndView("AgencyContract/index");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}


	// Add new agency contract page
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Thêm')")
	public ModelAndView addPage(Model model, Principal principal) {
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")){
			model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("listArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
			Long agencyId = user.getAgencyId();
			Agency agency = agencyService.getAgencyById(agencyId);
			if (agency != null){
				model.addAttribute("agency",agency);
				model.addAttribute("listArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
			}else {
				model.addAttribute("agency",null);
				model.addAttribute("listArea",null);
			}
		}
		else{
			model.addAttribute("listArea", null);
		}
		return new ModelAndView("AgencyContract/add");
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Thêm')")
	public ModelAndView addAgencyContract(@RequestParam Map<String, String> reParam,
										  @RequestParam("files[]") MultipartFile[] file, Principal principal, RedirectAttributes reDir) {
		ModelAndView modelAndView = new ModelAndView("redirect:/agencyContract/index");
			try {
				JSONObject json = new JSONObject(reParam);
				if (!json.getString("startDate").isEmpty() && !json.getString("endDate").isEmpty()) {
					Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("startDate"));
					Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("endDate"));
					AgencyContract lstAgencyContract = agencyContractRepository.getAgencyContractExist(json.getLong("agencyId"),startDate, endDate);
					if (lstAgencyContract != null){
						reDir.addFlashAttribute("error", "Bảo lãnh đặt cọc của đại lý " + json.getLong("agencyId") + " trong khoảng thời gian này đã có");
						return modelAndView;
					}
				}
				boolean rs =  service.createAgencyContract(json,principal,file);
				if (rs){
					reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				}else {
					reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				}
				return modelAndView;
			}catch (Exception e){
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				System.err.println(e);
				return modelAndView;
			}
	}

	// Edit agency contract page
	@GetMapping(value = "/edit/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Sửa')")
	public String editPage(Model model, @PathVariable("id") Long id , Principal principal) throws SQLException {
		AgencyContract ac = service.get(id);
		List<Agency> listAgency = agencyService.getAllAgency();
		model.addAttribute(LIST_AGENCY, listAgency);
		model.addAttribute("agencyContract", ac);
		model.addAttribute("imgContract", imgDepositsRepository.getAllFileAgencyContractId(id));
		model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		insertLogService.insertLog(principal.getName(),"/vasonline/agencyContract/index", ConstantLog.EDIT ,
				principal.getName()+" edit agency contract");
		return "AgencyContract/edit";
	}

	// Delete agency contract
	@GetMapping(value = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Xoá')")
	public ModelAndView deletePage(Model model, @PathVariable("id") Long id, Principal principal,RedirectAttributes reDir) {
		try {
			service.delete(id);
			insertLogService.insertLog(principal.getName(),"/vasonline/agencyContract/index", ConstantLog.DELETE ,
					principal.getName()+" delete agency contract success");
			reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/agencyContract/index");
		} catch (Exception e) {
			reDir.addFlashAttribute("message",ConstantNotify.FAILED);
			return new ModelAndView("redirect:/agencyContract/index");
		}
	}



	@PostMapping("/edit-agencyContract")
	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Sửa')")
	public ModelAndView editAgencyContract(@RequestParam Map<String, String> reParam,@RequestParam("files[]") MultipartFile[] file,
			 Principal principal2,RedirectAttributes reDir) throws SQLException {
		// add new agencyContract
		ModelAndView modelAndView = new ModelAndView("redirect:/agencyContract/index");

		try{
			JSONObject json = new JSONObject(reParam);
			System.err.println(Arrays.toString(file));
//		if (!json.getString("startDate").isEmpty() && !json.getString("endDate").isEmpty()) {
//			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("startDate"));
//			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("endDate"));
//			AgencyContract lstAgencyContract = agencyContractRepository.getAgencyContractExist(json.getLong("agencyAC"),startDate, endDate);
//			if (lstAgencyContract != null){
//
//				reDir.addFlashAttribute("error", "Bảo lãnh đặt cọc của đại lý " + json.getLong("agencyAC") + " trong khoảng thời gian này đã có");
//
//				return modelAndView;
//			}
//		}
			boolean rs =  service.editAgengyContract(json,principal2,file);
			if (rs){
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			}
			return modelAndView;

		}catch (Exception e){
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			System.err.println(e);
			return modelAndView;
		}
	}

	// Chức năng tra cứu hợp đồng đại lý
	@GetMapping("/search-agency-contract")
	@PreAuthorize("hasAnyAuthority('Tra cứu thông tin hợp đồng đại lý:Xem')")
	public ModelAndView searchAgencyContract(Model model, Principal principal) {
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if (levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		} else if (levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
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
		else {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
//		model.addAttribute("listAgency",	agencyService.getLIstAgencyByType(0));
//		model.addAttribute("listAgency", agencyRepository.findAgencyByType(0));
		ModelAndView modelAndView = new ModelAndView("SearchAgencyContract/search_agency_contract");
		modelAndView.addObject("page", "1");
		modelAndView.addObject("size", "5");
		return modelAndView;
	}


	// xuất file
	@GetMapping("/export-agency-contract")
//	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Xem')")
	public void exportContract(HttpServletResponse response, @RequestParam String agencyName, String status,
			String contractNo,String startDate, String endDate, Principal principal)
			throws IOException {
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/agencyContract/search-agency-contract", ConstantLog.SEARCH ,
					principal.getName()+" export agency contract");
			AgencyContract agencyContract = new AgencyContract();
			if (!agencyName.isEmpty()) {
				agencyContract.setAgencyAC(agencyService.getOne(Long.parseLong(agencyName)));
			}
			if (!status.isEmpty()) {
				agencyContract.setStatus(Integer.parseInt(status));
			} else {
				agencyContract.setStatus(100);
			}
			if (!startDate.isEmpty()) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
				agencyContract.setStartDate(start);
			}
			if (!endDate.isEmpty()) {
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
				agencyContract.setEndDate(end);
			}
			agencyContract.setContractNo(contractNo);
			List<AgencyContract> lstAgencyContracts = service.exportFile(response, agencyContract);
			response.setContentType("text/csv; charset=UTF-8");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String currentDateTime = dateFormatter.format(new Date());
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=agencyContract_" + currentDateTime + ".csv";
			response.setHeader(headerKey, headerValue);
			CsvBeanWriter csvWriter = new CsvBeanWriter((Writer) response.getWriter(),
					CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = new String[] { "CONTRACT_NO", "SERVICE_TYPE", "STATUS", "SIGN_DATE", "START_DATE",
					"END_DATE", "GUARANTEE_VALUE", "AGENCY_CODE" };
			String[] nameMapping = new String[] { "contractNo", "serviceType", "status", "signDate", "startDate",
					"endDate", "guaranteeValue", "agencyCode" };
			csvWriter.writeHeader(csvHeader);
			for (AgencyContract ac : lstAgencyContracts) {

				csvWriter.write(ac, nameMapping);
			}
			csvWriter.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@GetMapping(value = "/agency-list")
	@ResponseBody
	public List<Agency> listAgency(Model model, @RequestParam Long idAgencyArea) {
		System.out.println(idAgencyArea);
		return agencyService.getListAgencyById(idAgencyArea);
	}
}
