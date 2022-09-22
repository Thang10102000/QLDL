package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Users;
import com.neo.vas.repository.UsersRepository;
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
import org.springframework.web.servlet.ModelAndView;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 26/05/2021
 */
@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private AgencyBankService agencyBankService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private InsertLogService insertLogService;

    @Autowired
    private AgencyAreaService agencyAreaService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/bank-account")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Xem')")
    public ModelAndView getBankAccount(Model model, Principal principal) {
        //		Hiển thị ctkv theo từng account0
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
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

        ModelAndView modelAndView = new  ModelAndView("Agency/bank_account");
        modelAndView.addObject("page",1);
        modelAndView.addObject("size",10);
        return modelAndView;
    }

    @GetMapping("/new-bank-account")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Thêm')")
    public ModelAndView newBankAccount(Model model, Principal principal) {
        //		Hiển thị ctkv theo từng account0
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
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
        return new ModelAndView("Agency/new_bank_account", "bankAccount", agencyBankService.getAllBankAccount());
    }

    @PostMapping("/bank-account")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Thêm')")
    public ModelAndView newInformationAgency(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            boolean rs = bankAccountService.createBankAccount(jsonObject,principal);
            if (rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/bank-account", ConstantLog.CREATE,
                        principal.getName()+" create bank account success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/bank-account");
            }else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/bank-account");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/bank-account");
        }
    }

    @GetMapping("/edit-bank-account/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Sửa')")
    public ModelAndView editBankAccount(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("agency", agencyService.getLIstAgencyByType(0));
        model.addAttribute("agencyBank",agencyBankService.getAgencyBankAccount(id));
        return new ModelAndView("Agency/edit_bank_account", "editBank", bankAccountService.getBankAccountById(id));
    }

    @PostMapping("/edit-bank-account")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Sửa')")
    public ModelAndView updateBankAccount(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
           boolean rs = bankAccountService.updateBankAccount(jsonObject,principal);
            if (rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/bank-account", ConstantLog.EDIT,
                        principal.getName()+" edit bank account "+jsonObject.getString("id")+" success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/bank-account");
            }else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/bank-account");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/bank-account");
        }
    }

    @GetMapping("/delete-bank-account/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản ngân hàng đại lý:Xoá')")
    public ModelAndView deleteBankAccount(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir) throws SQLException {
        this.bankAccountService.deleteById(id);
        insertLogService.insertLog(principal.getName(),"/vasonline/bank-account", ConstantLog.DELETE,
                principal.getName()+" delete bank account "+ id +" success");
        reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
        return new ModelAndView("redirect:/bank-account");
    }
}
