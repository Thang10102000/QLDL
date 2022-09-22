package com.neo.vas.controller;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.*;
import com.neo.vas.repository.*;
import com.neo.vas.service.*;
import com.neo.vas.service.impl.AgencyServiceImpl;
import com.neo.vas.service.impl.DepositeServiceImpl;
import com.neo.vas.service.impl.ImgDepositsServiceImpl;
import com.neo.vas.service.impl.UsersServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * @author DatND
 *
 */
@Controller
@RequestMapping("/deposite")
public class DepositeController {

	@Autowired
	private DepositeService service;
	@Autowired
	private AgencyService agencyService;
	@Autowired
	private ImgDepositsService imgDepositsService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private DepositeService depositeService;

	private static final String LIST_AGENCY = "listAgency";

	// Index page, get all country, display 5 records
	@GetMapping(value = "/index")
	@PreAuthorize("hasAnyAuthority('Quản lý bảo lãnh - đặt cọc:Xem')")
	public ModelAndView indexPage(Model model, Principal principal){
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
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
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		ModelAndView modelAndView = new ModelAndView("Deposite/index");
		modelAndView.addObject("page", 1);
		modelAndView.addObject("size", 5);
		return modelAndView;
	}

	// Add new agency contract page
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('Quản lý bảo lãnh - đặt cọc:Thêm')")
	public String addPage(Model model, Principal principal) {
//		Deposits deposits = new Deposits();
//		List<Agency> listAgency = agencyService.getAllAgency();
//		model.addAttribute(LIST_AGENCY, listAgency);
//		model.addAttribute("deposits", deposits);
//		model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")){
			model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("listArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else if(levelName.equals("Đại lý")){
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
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		return "Deposite/add";
	}

	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('Quản lý bảo lãnh - đặt cọc:Thêm')")
	public ModelAndView addDeposits(@RequestParam Map<String, String> reParam,
									@RequestParam("files[]") MultipartFile[] file, Principal principal, RedirectAttributes reDir) {
		ModelAndView mv = new ModelAndView("redirect:/deposite/index");
			try {
				JSONObject json = new JSONObject(reParam);
//				check trùng
				if (!json.getString("startDate").isEmpty() && !json.getString("endDate").isEmpty()) {
					Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("startDate"));
					Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("endDate"));
					Deposits lstDeposit = depositeService.getDepositsExist(json.getLong("agencyId"),startDate,endDate);
					if (lstDeposit != null){
						reDir.addFlashAttribute("error", "Bảo lãnh đặt cọc của đại lý " + json.getLong("agencyId") + " trong khoảng thời gian này đã có");
						return mv;
					}
				}
				boolean rs =  service.createDeposit(json,principal,file);
				if (rs){
					reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				}else {
					reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				}
				return mv;
			}catch (Exception e){
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				System.err.println(e);
				return mv;
			}
	}

	// Edit agency contract page
	@GetMapping(value = "/edit/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý bảo lãnh - đặt cọc:Sửa')")
	public String editPage(Model model, @PathVariable("id") Long id, Principal principal) throws SQLException {
		Deposits dep = service.get(id);
		List<Agency> listAgency = agencyService.getAllAgency();
		List<AgencyContract> lstContract = service.getAgencyContractByAgencyId(dep.getAgencyD().getId());
		model.addAttribute(LIST_AGENCY, listAgency);
		model.addAttribute("deposite", dep);
		model.addAttribute("imgDeposit", imgDepositsService.getAllFileDeposit(id));
		model.addAttribute("listArea", agencyAreaService.getAllAgencyArea());
		model.addAttribute("lstContract", lstContract);
		insertLogService.insertLog(principal.getName(), "/vasonline/deposite/index", ConstantLog.EDIT,
				principal.getName() + " edit deposit");
		return "Deposite/edit";
	}

	// Delete agency contract
	@GetMapping(value = "/delete/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý bảo lãnh - đặt cọc:Xoá')")
	public ModelAndView deletePage( @PathVariable("id") Long id, Principal principal, RedirectAttributes reDir) {
		ModelAndView mv = new ModelAndView("redirect:/deposite/index");
		try {
			service.delete(id);
			insertLogService.insertLog(principal.getName(), "/vasonline/deposite/index", ConstantLog.DELETE,
					principal.getName() + " delete deposit");
			reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return mv;
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return mv;
		}

	}
	
	
	@PostMapping(value = "/edit-deposits")
	public ModelAndView editDeposit(@RequestParam Map<String, String> reParam,
									@RequestParam("files[]") MultipartFile[] file, Principal principal, RedirectAttributes reDir) {
		ModelAndView mv = new ModelAndView("redirect:/deposite/index");
		try {
			JSONObject json = new JSONObject(reParam);
			System.err.println(json);
			//check trùng
//			if (!json.getString("startDate").isEmpty() && !json.getString("endDate").isEmpty()) {
//				Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("startDate"));
//				Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(json.getString("endDate"));
//				Deposits lstDeposit = depositeRepository.getDepositsExist(json.getLong("agencyD"),startDate,endDate);
//				if (lstDeposit != null){
//					reDir.addFlashAttribute("error", "Bảo lãnh đặt cọc của đại lý " + json.getLong("agencyD") + " trong khoảng thời gian này đã có");
//					return mv;
//				}
//			}

			boolean rs =  service.editDeposit(json,principal,file);
			if (rs){
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			}
			return mv;
		}catch (Exception e){
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			System.err.println(e);
			return mv;
		}
	}
	
	@GetMapping(value = "/agency-list")
    @ResponseBody
    public List<Agency> listAgency(Model model, @RequestParam Long idAgencyArea) {
	 System.out.println(idAgencyArea);
        List<Agency> list = agencyService.getAgencyByArea(idAgencyArea);
        return list;
    }

//    get agency contract by agency id
	@GetMapping(value = "/agency-contract-by-agency-id")
	@ResponseBody
	public List<AgencyContract> getAgencyContract(@RequestParam Long agencyId){
		return depositeService.getAgencyContractByAgencyId(agencyId);
	}


//    search deposit
	@GetMapping("search-deposit")
	@PreAuthorize("hasAnyAuthority('Tra cứu thông tin bảo lãnh - đặt cọc:Xem')")
	public ModelAndView searchDeposit(Model model,Principal principal){
		String username = principal.getName();
		Users user = usersService.getUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
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
		ModelAndView mav = new ModelAndView("SearchDeposit/searchDeposit");
		mav.addObject("page", 1);
		mav.addObject("size", 5);
		return mav;
	}


	// xuất file
	@GetMapping("/export-deposit")
//	@PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Xem')")
	public void exportContract(HttpServletResponse response, @RequestParam String agencyName, String depositsAmount,
							   String startDate, String endDate, String status, Principal principal)
			throws IOException {
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/agencyContract/search-agency-contract", ConstantLog.SEARCH ,
					principal.getName()+" export agency contract");

			List<Deposits> lstAgencyContracts = service.exportFile(response, agencyName,depositsAmount,startDate,endDate,status);
			response.setContentType("text/csv; charset=UTF-8");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String currentDateTime = dateFormatter.format(new Date());
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=agencyContract_" + currentDateTime + ".csv";
			response.setHeader(headerKey, headerValue);
			CsvBeanWriter csvWriter = new CsvBeanWriter((Writer) response.getWriter(),
					CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = new String[] { "AGENCY_CODE", "DEPOSIT_AMOUNT", "STATUS", "START_DATE","END_DATE" };
			String[] nameMapping = new String[] { "agencyCode", "depositsAmount", "status",  "startDate",
					"endDate"};
			csvWriter.writeHeader(csvHeader);
			for (Deposits ac : lstAgencyContracts) {
				System.err.println("agency code "+ ac.getAgencyCode() );
				csvWriter.write(ac, nameMapping);
			}
			csvWriter.close();
		} catch (Exception e) {
			System.err.println("Loi tai controller: " + e);
		}
	}

}
