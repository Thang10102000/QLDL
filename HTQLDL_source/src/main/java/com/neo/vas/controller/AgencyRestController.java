package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.*;
import com.neo.vas.dto.AgencyOrdersDTO;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * project_name: vasonline2021 Created_by: thulv time: 04/06/2021
 */
@RestController
public class AgencyRestController {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private AgencyAreaService agencyAreaService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private OrdersStatusService ordersStatusService;

    @Autowired
    private AgencyOrdersService agencyOrdersService;

    @Autowired
    private AgencyContractService agencyContractService;

    @Autowired
    private InsertLogService insertLogService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("agency-search")
    @PreAuthorize("hasAnyAuthority('Quản lý đại lý:Xem')")
    public Map<JSONObject, Integer> searchAgency(@RequestParam String agencyName, String areaId,
                                                 String status, String type, @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }

        Map<JSONObject, Integer> data = new LinkedHashMap<>();
        try {
            if (!agencyName.isEmpty()) {
                agencyName = agencyName.toLowerCase().trim();
            }
            if (!areaId.isEmpty()) {
                areaId = areaId.toLowerCase().trim();
            }
            if (!status.isEmpty()) {
                status = status.toLowerCase().trim();
            }
            if (!type.isEmpty()) {
                type = type.toLowerCase().trim();
            }
            Page<Agency> pageAgency = agencyService.searchAgency(agencyName, areaId, status, type, realPage, size);
            for (Agency ag : pageAgency) {
                JSONObject js = ag.createJson();
                data.put(js, pageAgency.getTotalPages());
            }
            insertLogService.insertLog(principal.getName(), "/vasonline/agency", ConstantLog.SEARCH, principal.getName() + " search agency");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("bank-account-search")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Xem')")
    public Map<JSONObject, Integer> searchBankAccount(@RequestParam String agencyId,
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
            Page<AgencyBankAccount> pageAgencyBank = bankAccountService.searchBankAccount(agencyId, realPage, size);
            for (AgencyBankAccount ab : pageAgencyBank) {
                JSONObject js = ab.createJson();
                data.put(js, pageAgencyBank.getTotalPages());
            }
            insertLogService.insertLog(principal.getName(), "/vasonline/bank-account", ConstantLog.SEARCH, principal.getName() + " search bank account");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/agency-order-search")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xem')")
    public Map<JSONObject, Integer> searchAgencyOrder(@RequestParam String agencyName, String agencyArea, String orderId, String status,
                                                      String startDate, String endDate, String paymentMethod,
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
            insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.SEARCH, principal.getName() + " search agency order");
            AgencyOrders agencyOrders = new AgencyOrders();
            if (!agencyName.isEmpty()) {
                agencyOrders.setAgencyAO(agencyService.getOne(Long.parseLong(agencyName)));
//				agencyOrders.setAgencyAO(agencyRepository.findById(Long.parseLong(agencyName)));
            }
            if (!orderId.isEmpty()) {
                agencyOrders.setOrderId(Integer.parseInt(orderId));
            } else {
                agencyOrders.setOrderId(0);
            }
            if (!status.isEmpty()) {
                agencyOrders.setOrderStatusAO(ordersStatusService.findById(Long.parseLong(status)));
            }
            if (!startDate.isEmpty()) {
                Date start = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                agencyOrders.setStartDate(start);
            }
            if (!endDate.isEmpty()) {
                Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
                agencyOrders.setEndDate(end);
            }
            if (!paymentMethod.isEmpty()) {
                agencyOrders.setPaymentMethod(Long.parseLong(paymentMethod));
            } else {
                agencyOrders.setPaymentMethod(1000);
            }
            if (!Pattern.matches("\\d+", agencyArea))
                agencyArea = "";
            Users user = usersRepository.findUsersByUsername(principal.getName());
            Page<AgencyOrdersDTO> agencyOrdersPage = agencyOrdersService.searchAgencyOrder(agencyOrders, agencyArea, realPage, size);
            for (AgencyOrdersDTO ao : agencyOrdersPage) {
                JSONObject json = ao.createJson();
                json.put("levelId", user.getLevelUsers().getLevelId());
                data.put(json, agencyOrdersPage.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/pay-confirm-search")
    @PreAuthorize("hasAnyAuthority('Quản lý đơn hàng đại lý:Xem')")
    public Map<JSONObject, Integer> searchPayConfirm(@RequestParam String orderId, String paymentMethod, String amount,
                                                     String receiverName, String receiverDate,
                                                     @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                     @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }
        HashMap<JSONObject, Integer> data = new HashMap<>();
        try {
            insertLogService.insertLog(principal.getName(), "/vasonline/agency-order", ConstantLog.SEARCH,
                    principal.getName() + " search pay confirm agency order");
            AgencyOrderPayment agencyOrderPayment = new AgencyOrderPayment();
            if (!orderId.isEmpty()) {
                agencyOrderPayment.setAgencyOrdersAOP(agencyOrdersService.getOne(Long.parseLong(orderId)));
            }
            if (!amount.isEmpty()) {
                agencyOrderPayment.setAmount(Long.parseLong(amount));
            } else {
                agencyOrderPayment.setAmount(0);
            }
            if (!receiverDate.isEmpty()) {
                Date receiver = new SimpleDateFormat("dd/MM/yyyy").parse(receiverDate);
                agencyOrderPayment.setReceiverTime(receiver);
            }
            agencyOrderPayment.setPaymentMethod(paymentMethod);
            agencyOrderPayment.setReceiverName(receiverName);
            Page<AgencyOrderPayment> agencyOrderPaymentPage = agencyOrdersService.searchPayment(agencyOrderPayment,
                    realPage, size);
            for (AgencyOrderPayment age : agencyOrderPaymentPage) {
                JSONObject json = age.createJson();
                data.put(json, agencyOrderPaymentPage.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // AgencyContract search
    @GetMapping("/agency-contract-search")
    @PreAuthorize("hasAnyAuthority('Tra cứu thông tin hợp đồng đại lý:Xem')")
    public Map<JSONObject, Integer> searchAgencyContract(@RequestParam String contractNo, String agencyName,
                                                         String status, String startDate, String endDate,
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
            insertLogService.insertLog(principal.getName(), "/vasonline/agencyContract/search-agency-contract", ConstantLog.SEARCH,
                    principal.getName() + " search agency contract");

            Page<AgencyContract> agencyContractPage = agencyContractService.searchAgencyContract(contractNo, agencyName, status, startDate, endDate, realPage, size);
            for (AgencyContract ac : agencyContractPage) {
                JSONObject json = ac.createJson();
                data.put(json, agencyContractPage.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // agency contract management
    @GetMapping("/search-contract-agency")
    @PreAuthorize("hasAnyAuthority('Quản lý hợp đồng đại lý:Xem')")
    public Map<JSONObject, Integer> searchContractAgency(@RequestParam String contractNo, String agencyName,
                                                         String status, String startDate, String endDate,
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
            insertLogService.insertLog(principal.getName(), "/vasonline/agencyContract/search-agency-contract", ConstantLog.SEARCH,
                    principal.getName() + " search agency contract");

            Page<AgencyContract> agencyContractPage = agencyContractService.searchAgencyContract(contractNo, agencyName, status, startDate, endDate, realPage, size);
            for (AgencyContract ac : agencyContractPage) {
                JSONObject json = ac.createJson();
                data.put(json, agencyContractPage.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("area-search")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Xem')")
    public Map<JSONObject, Integer> searchArea(@RequestParam String areaCode, String areaName, String taxCode, String description,
                                               @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                               @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
        int realPage = page - 1;
        if (realPage - 1 < 0) {
            realPage = 0;
        }
        if (size < 0) {
            size = 5;
        }
        Map<JSONObject, Integer> data = new LinkedHashMap<>();
        try {
            if (!areaCode.isEmpty()) {
                areaCode = areaCode.trim();
            }
            if (!areaName.isEmpty()) {
                areaName = areaName.trim();
            }
            if (!taxCode.isEmpty()) {
                taxCode = taxCode.trim();
            }
            if (!description.isEmpty()) {
                description = description.trim();
            }
            Page<AgencyArea> pageArea = agencyAreaService.searchAgencyArea(areaCode, areaName, taxCode, description, realPage, size);
            for (AgencyArea ag : pageArea) {
                JSONObject js = ag.jsonObject();
                data.put(js, pageArea.getTotalPages());
            }
            insertLogService.insertLog(principal.getName(), "/vasonline/area", ConstantLog.SEARCH, principal.getName() + " search agency");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
