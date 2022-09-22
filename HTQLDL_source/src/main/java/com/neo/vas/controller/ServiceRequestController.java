package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.constant.ConstantStatusOrder;
import com.neo.vas.constant.ConstantStatusSR;
import com.neo.vas.domain.*;
import com.neo.vas.service.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
public class ServiceRequestController {
    @Autowired
    private AgencyAreaService agencyAreaService;
    @Autowired
    private ServiceInterface serviceInterface;
    @Autowired
    private BrandGroupService bgService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CustomerService cusService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private ServiceRequestService srService;
    @Autowired
    private FilesAgencyOrderService faoService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private ServiceRequestService serviceRequestService;

    @GetMapping(value = {"/service-request"})
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Xem')")
    public ModelAndView listAsr(Model model, Principal principal) throws SQLException {
        String username = principal.getName();
        Users user = usersService.getUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
        } else if (levelName.equals("AM/KAM") || levelName.equals("Đại lý")) {
            Agency agency = agencyService.getAgencyById(user.getAgencyId());
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
        } else {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        }
        model.addAttribute("services", serviceInterface.getAllService());
        return new ModelAndView("ServiceRequest/service_request");
    }

    @GetMapping("/service-request/create")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thêm')")
    public ModelAndView getAllAsr(Model model, Principal principal) {
        model.addAttribute("services", serviceInterface.getAllService());
        String username = principal.getName();
        Users user = usersService.getUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
        } else if (levelName.equals("AM/KAM") || levelName.equals("Đại lý")) {
            Agency agency = agencyService.getAgencyById(user.getAgencyId());
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
        } else {
            model.addAttribute("agencyArea", null);
        }
        return new ModelAndView("ServiceRequest/service_request_create");
    }

    @PostMapping("/service-request/create")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thêm')")
    public String doCreateAsr(@RequestParam Map<String, String> reqParam, Principal principal,
                              @RequestParam("contract_files[]") MultipartFile[] contractFiles,
                              @RequestParam("pay_files[]") MultipartFile[] payFiles,
                              @RequestParam("scan_files[]") MultipartFile[] scanFiles, Model model, RedirectAttributes redir)
            throws SQLException {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            String res = srService.saveNewSr(reqParamObj, principal, contractFiles, payFiles, scanFiles);
            if (res.equals("INVALID_PARAMS")) {
                redir.addFlashAttribute("message", "INVALID_PARAMS");
                return "redirect:/service-request";
            } else if (res.equals("ERROR_FILE")) {
                redir.addFlashAttribute("message", "ERROR_FILE");
                return "redirect:/service-request";
            } else if (res.equals("TRUE")) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.CREATE,
                        principal.getName() + " create service request");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping("/service-request/edit/{requestId}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Sửa')")
    public ModelAndView getAsrEdit(@PathVariable("requestId") long requestId, Model model, Principal principal) {
        model.addAttribute("services", serviceInterface.getAllService());
        ServiceRequest sr = srService.getSrById(requestId);
        AgencyArea aa = sr.getCustomer().getAreaCId();
        List<Customer> cus = cusService.getCustomerByAreaId(aa.getAreaId());
        model.addAttribute("customers", cus);
        model.addAttribute("contractFiles", faoService.getContractFileBySrId(requestId));
        model.addAttribute("payFiles", faoService.getPayFileBySrId(requestId));
        model.addAttribute("scanFiles", faoService.getScanFileBySrId(requestId));
        List<Services> sList = new ArrayList<Services>();
        sList.add(sr.getBrandASR().getBrandGroupB().getServicesBG());
        List<BrandGroup> brList = bgService.getListBGByListService(sList);
        List<Brand> bList = brandService.getListBrandByListBG(brList);
        model.addAttribute("packageGroups", brList);
        model.addAttribute("packages", bList);

        String username = principal.getName();
        Users user = usersService.getUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(areaId));
        } else if (levelName.equals("AM/KAM")  || levelName.equals("Đại lý")) {
            Agency agency = agencyService.getAgencyById(user.getAgencyId());
            model.addAttribute("agencyArea", agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
        } else {
            model.addAttribute("agencyArea", null);
        }

        return new ModelAndView("ServiceRequest/service_request_edit", "srData", srService.getSrById(requestId));
    }

    @PostMapping("/service-request/edit")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Sửa')")
    public String doUpdateAsr(@RequestParam Map<String, String> reqParam, Principal principal,
                              @RequestParam("contract_files[]") MultipartFile[] contractFiles,
                              @RequestParam("pay_files[]") MultipartFile[] payFiles,
                              @RequestParam("scan_files[]") MultipartFile[] scanFiles, Model model, RedirectAttributes redir)
            throws SQLException {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            String res = srService.editSr(reqParamObj, principal, contractFiles, payFiles, scanFiles);
            if (res.equals("INVALID_PARAMS")) {
                redir.addFlashAttribute("message", "INVALID_PARAMS");
                return "redirect:/service-request";
            } else if (res.equals("TRUE")) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.EDIT,
                        principal.getName() + " update service request");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping("/service-request/delete/{requestId}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Xoá')")
    public String doDeleteAsr(@PathVariable("requestId") long requestId, Principal principal, RedirectAttributes redir)
            throws SQLException {
        if (srService.deleteSr(requestId)) {
            insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.DELETE,
                    principal.getName() + " delete service request");
            redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return "redirect:/service-request";
        } else {
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping("/service-request/approval/{requestId}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public ModelAndView getActionSr(@PathVariable("requestId") long requestId, Model model, Principal principal) {

        return new ModelAndView("ServiceRequest/service_request_action", "srData", srService.getSrById(requestId));
    }

    @PostMapping("/service-request/approval")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public String doActionSr(@RequestParam Map<String, String> reqParam, Principal principal, Model model,
                             RedirectAttributes redir) throws SQLException {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            boolean res = srService.actionSr(reqParamObj, principal);
            if (res) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.EXECUTE,
                        principal.getName() + " action service request");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping(value = "/service-request/get-price")
    @ResponseBody
    public Long getPrice(@RequestParam String policyCode, @RequestParam String brandId) {
        return srService.getPrice(Long.parseLong(brandId), policyCode);
    }

    @GetMapping(value = "/service-request/get-discount")
    @ResponseBody
    public Long getDiscount(@RequestParam String policyCode, @RequestParam String brandId) {
        return srService.getDiscount(Long.parseLong(brandId), policyCode);
    }

    @GetMapping(value = "/service-request/get-price-discount")
    @ResponseBody
    public String getPriceDiscount(@RequestParam String policyCode, @RequestParam String brandId) {
        return srService.getPriceDiscount(Long.parseLong(brandId), policyCode);
    }

    @GetMapping("/delete-file-sr/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Sửa')")
    public ResponseEntity<String> deleteFile(@PathVariable(value = "id") long id, Principal principal,
                                             RedirectAttributes reDir) throws SQLException {
        faoService.deleteById(id);
        insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.DELETE,
                principal.getName() + " delete file service request");
        reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
        return new ResponseEntity<>(ConstantNotify.SUCCESS, HttpStatus.OK);
    }

    @GetMapping("/service-request/accept/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public String acceptRequest(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir)
            throws SQLException {
        try {
            boolean res = srService.acceptSr(id, principal);
            if (res) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.EXECUTE,
                        principal.getName() + " forward service request");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping("/service-request/cancel/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public String changStatusCancel(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir)
            throws SQLException {
        try {
            boolean res = srService.changeSr(id, principal);
            if (res) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.EXECUTE,
                        principal.getName() + " forward service request");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping("/service-request/activate/{requestId}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public ModelAndView getActivateSr(@PathVariable("requestId") long requestId, Model model, Principal principal) {

        return new ModelAndView("ServiceRequest/service_request_activate", "srData", srService.getSrById(requestId));
    }

    @PostMapping("/service-request/activate")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public String doActivateSr(@RequestParam Map<String, String> reqParam, Principal principal, Model model,
                               RedirectAttributes redir) throws SQLException {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            String res = srService.activateSr(reqParamObj, principal);
            if (res.equals("INVALID_ORDER")) {
                redir.addFlashAttribute("message", "INVALID_ORDER");
                return "redirect:/service-request";
            } else if (res.equals("ORDER_OUT_OF_MONEY")) {
                redir.addFlashAttribute("message", "ORDER_OUT_OF_MONEY");
                return "redirect:/service-request";
            } else if (res.equals("ORDER_NOT_COMPLETE")) {
                redir.addFlashAttribute("message", "ORDER_NOT_COMPLETE");
                return "redirect:/service-request";
            } else if (res.equals("FALSE")) {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            } else if (res.equals("BAD REQUEST")) {
                redir.addFlashAttribute("message", "BAD REQUEST");
                return "redirect:/service-request";
            }else if (res.equals("ERROR AUTHENTICATION")) {
                redir.addFlashAttribute("message", "ERROR AUTHENTICATION");
                return "redirect:/service-request";
            }else if (res.equals("TRANSACTION DUPLICATE")) {
                redir.addFlashAttribute("message", "TRANSACTION DUPLICATE");
                return "redirect:/service-request";
            }else if (res.equals("ERROR SYSTEM")) {
                redir.addFlashAttribute("message", "ERROR SYSTEM");
                return "redirect:/service-request";
            }else if (res.equals("SUCCESS")) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.EXECUTE,
                        principal.getName() + " activate service request");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }

    @GetMapping("/service-request/list-active/{requestId}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public ModelAndView getListActivateSr(@PathVariable("requestId") long requestId, Model model, Principal principal) {

        return new ModelAndView("ServiceRequest/service_request_list_act", "listActive", srService.getSrById(requestId).getSrActive());
    }

    @GetMapping("/service-request/recharge-order/{sraId}")
    @PreAuthorize("hasAnyAuthority('Quản lý yêu cầu dịch vụ MobiEdu:Thực thi')")
    public String doRechargeSr(@PathVariable("sraId") long sraId, Principal principal,
                               RedirectAttributes redir) throws SQLException {
        try {
            boolean res = srService.rechargeAsr(sraId, principal);
            if (res) {
                insertLogService.insertLog(principal.getName(), "/vasonline/service-request", ConstantLog.EXECUTE,
                        principal.getName() + " recharge service request");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/service-request";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/service-request";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/service-request";
        }
    }
}
