package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

import com.neo.vas.constant.ConstantStatusOrder;
import com.neo.vas.service.AgencyOrdersService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.repository.TransactionApiRepository;
import com.neo.vas.service.TransactionApiService;

/**
 * @author YNN
 *
 */

@Controller
public class TransactionApiController {
	@Autowired
	private TransactionApiRepository taRepository;
	@Autowired
	private TransactionApiService taService;
	@Autowired
	private AgencyOrdersService agencyOrdersService;
	
	@GetMapping(value = { "/transaction-api" })
	public ModelAndView listApiLog(Model model) throws SQLException {
		model.addAttribute("logs", taRepository.findAll());
//		return new ModelAndView("TransactionApi/ApiTransactionLog");
		return new ModelAndView("TransactionApi/api_transaction");
	}

	@GetMapping("/transaction-api/create")
	public ModelAndView callApi(Model model) {
		return new ModelAndView("TransactionApi/create");
	}
	
	@PostMapping("/transaction-api/create")
	public String doCreateAsr(@RequestParam Map<String, String> reqParam, Principal principal, RedirectAttributes redir)
			throws SQLException {
		try {
			JSONObject reqParamObj = new JSONObject(reqParam);
			boolean res = taService.callApi(reqParamObj, principal);
			if(res) {
				redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return "redirect:/transaction-api";
			}
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/transaction-api";
		} catch (Exception e) {
			System.err.println(e);
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/transaction-api";
		}
	}

	@GetMapping(value = { "/transaction-search" })
	@PreAuthorize(value = "hasAnyAuthority('Tra cứu lịch sử đối soát:Xem')")
	public ModelAndView showAll(Model model) throws SQLException {
		return new ModelAndView("TransactionApi/ApiTransactionLog");
	}

//	call api agency order
@PostMapping("/transaction-api/agency-order/create")
public String doCallApiAgencyOder(@RequestParam Map<String, String> reqParam, Principal principal, RedirectAttributes redir)
		throws SQLException {
	try {
		JSONObject reqParamObj = new JSONObject(reqParam);
		System.err.println(reqParamObj);
		String res = taService.callApiInvoice(reqParamObj, principal);
		if(res.equals("STATUS200")) {
			this.agencyOrdersService.changeStatus(reqParamObj.getLong("orderId"), ConstantStatusOrder.INVOICED, principal);
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/agency-order";
		} else if (res.equals("STATUS400")){
			redir.addFlashAttribute("message", "HTTP Status 400 – Bad Request");
			return "redirect:/agency-order";
		} else if (res.equals("FALSE")){
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/agency-order";
		}else {
			redir.addFlashAttribute("message", res);
			return "redirect:/agency-order";
		}
	} catch (Exception e) {
		System.err.println(e);
		redir.addFlashAttribute("message", ConstantNotify.FAILED);
		return "redirect:/agency-order";
	}
}
}
