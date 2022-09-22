/**
 * 
 */
package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.AMKAM;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Users;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.AgencyService;
import com.neo.vas.service.AmkamService;
import com.neo.vas.service.InsertLogService;
import java.io.*;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * @author YNN
 *
 */
@RestController
public class AmkamController {
	@Autowired
	private AmkamService amkamService;
	@Autowired
	private AgencyAreaService agencyAreaService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private AgencyService agencyService;

	@GetMapping(value = { "/amkam" })
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Xem')")
	public ModelAndView listAmkam(Model model, Principal principal) {
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		return new ModelAndView("/Amkam/amkam");
	}


	@GetMapping("/edit-am/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Sửa')")
	public ModelAndView editAgency(@PathVariable(value = "id") Long id, Model model) {
		ModelAndView mav = new ModelAndView("/Amkam/edit_am");
		model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		mav.addObject("editAm",agencyService.getAgencyById(id));
		return mav;
	}

	@PostMapping("/edit-kam")
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Sửa')")
	public ModelAndView updateAgency(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir){
		try {
			JSONObject jsonObject = new JSONObject(reParam);
			boolean result = amkamService.updateAmKam(jsonObject, principal);
			String description = "";
			ModelAndView modelAndView= new ModelAndView("redirect:/amkam");
			if (result){
				description = principal.getName() + " edit amkam success";
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			}
			insertLogService.insertLog(principal.getName(),"/vasonline/amkam", ConstantLog.EDIT,description);
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/amkam");
		}
	}

	@GetMapping(value = { "/amkam-export" })
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Xem')")
	public void exportToCSV(HttpServletResponse response, @RequestParam String agencyCode, String agencyName,
			String areaId, Principal principal) throws Exception {
		response.setContentType("text/csv; charset=UTF-8");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=amkam_" + currentDateTime + ".csv";
		response.setHeader(headerKey, headerValue);
		CsvBeanWriter csvWriter = new CsvBeanWriter((Writer) response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = new String[] { "AMKAM_CODE", "AMKAM_NAME", "CTKV_ID", "CTKV_NAME" };
		String[] nameMapping = new String[] { "amkamCode", "amkamName", "areaId", "areaName" };
		List<AMKAM> amkams = amkamService.exportAmkam(agencyCode, agencyName, areaId);
		csvWriter.writeHeader(csvHeader);
		for (AMKAM amkam : amkams) {
			csvWriter.write(amkam, nameMapping);
		}
		csvWriter.close();
		insertLogService.insertLog(principal.getName(), "/vasonline/amkam", ConstantLog.SEARCH,
				principal.getName() + " export amkam list");
	}

	@GetMapping("amkam-search")
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Xem')")
	public Map<JSONObject, Integer> searchAgency(@RequestParam String agencyCode, String agencyName, String areaId, String shopCode,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/amkam", ConstantLog.SEARCH,
					principal.getName() + " search amkam list");
			Page<Agency> pageAgency = amkamService.searchAmkam(agencyCode, agencyName, areaId,shopCode, realPage, size);
			for (Agency ag : pageAgency) {
				JSONObject js = ag.createJson();
				data.put(js, pageAgency.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("new-amkam")
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Thêm')")
	public ModelAndView newAmKam(Model model, Principal principal) {
		System.out.println("get new amkam");
		String username = principal.getName();
		Users user = usersRepository.findUsersByUsername(username);
		String levelName = user.getLevelUsers().getLevelName();
		if(levelName.equals("MDS")) {
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		}
		else if(levelName.equals("Công ty khu vực")) {
			Long areaId = user.getAreaId().getAreaId();
			model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
		}
		else
			model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
		return new ModelAndView("/Amkam/new-amkam");
	}

	@PostMapping("new-amkam")
	@PreAuthorize("hasAnyAuthority('Quản lý AM/KAM:Thêm')")
	public ModelAndView createAmkam(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
		System.out.println("post new amkam");
		try {
			String description = "";
			ModelAndView mav = new ModelAndView("redirect:/amkam");
			JSONObject jsonObject = new JSONObject(reParam);
			Agency agency = agencyService.getAgencyByCode(jsonObject.getString("amkamCode"));
			if(agency != null){
				reDir.addFlashAttribute("exist", ConstantNotify.FAILED);
				return mav;
			}
			boolean result = amkamService.createAmkam(jsonObject,principal);
			if (result){
				description = principal.getName() + " create amkam success";
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			}else {
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			}
			insertLogService.insertLog(principal.getName(),"/vasonline/amkam", ConstantLog.CREATE, description);
			return mav;
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/amkam");
		}
	}

	@GetMapping("/delete-amkam/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý đại lý:Xoá')")
	public ModelAndView deleteAmkam(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir){
		String description = "";
		try {
			this.agencyService.deleteAgency(id);
			description = principal.getName() + " delete amkam success";
			insertLogService.insertLog(principal.getName(),"/vasonline/amkam", ConstantLog.DELETE ,description);
			reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return new ModelAndView("redirect:/amkam");
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return new ModelAndView("redirect:/amkam");
		}
	}

}
