package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.*;
import com.neo.vas.repository.SystemFunctionalRepository;
import com.neo.vas.repository.UahRepository;
import com.neo.vas.repository.UsersLevelsFunctionalRepository;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SystemFunctionalService;
import com.neo.vas.service.UserLevelFunctionalService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

@Controller
public class SystemFunctionalController {
    @Autowired
    private SystemFunctionalService systemFunctionalService;

    @Autowired
    private InsertLogService insertLogService;

    @Autowired
    private SystemFunctionalRepository systemFunctionalRepository;

    @Autowired
    private UserLevelFunctionalService userLevelFunctionalService;

    @Autowired
    private UsersLevelsFunctionalRepository usersLevelsFunctionalRepository;

    @Autowired
    private  UahRepository UahRepository;

    @GetMapping("/admin/system-functional")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách hệ thống:Xem')")
    public ModelAndView listSF() {
        return new ModelAndView("SystemFunctional/systemfunctional-search");
    }

    @GetMapping("/admin/system-functional/edit/{sfId}")
    @PreAuthorize("hasAnyAuthority('Quản lý chức năng hệ thống:Sửa')")
    public ModelAndView getSFEdit(@PathVariable("sfId") Long sfId, Model model) {
        SystemFunctional sf = systemFunctionalRepository.findSystemFunctionalBySfId(sfId);
        model.addAttribute("sf", sf);
        List<String> listsfName = systemFunctionalRepository.findSFNameByStatus(0);
        model.addAttribute("listsfName", listsfName);
        return new ModelAndView("SystemFunctional/systemfunctional-edit", "sf", sf);
    }

    @GetMapping("/admin/system-functional/create")
    @PreAuthorize("hasAnyAuthority('Quản lý chức năng hệ thống:Thêm')")
    public ModelAndView getSFCreate(Principal principal, Model model) {
        return new ModelAndView("SystemFunctional/systemfunctional-create");
    }

    @PostMapping("/admin/system-functional/create")
    @PreAuthorize("hasAnyAuthority('Quản lý chức năng hệ thống:Thêm')")
    public String doCreateSF(@RequestParam Map<String, String> reqParam, Principal principal, Model model,
                               RedirectAttributes redir) throws SQLException {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            boolean result = systemFunctionalService.saveNewSF(reqParamObj, principal);
            if (result) {
                insertLogService.insertLog(principal.getName(), "/vasonline/admin/system-functional", ConstantLog.CREATE,
                        principal.getName() + " create sf success");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/admin/system-functional";
            }else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/admin/system-functional";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/admin/system-functional";
        }
    }


    @PostMapping("/admin/system-functional/edit")
    @PreAuthorize("hasAnyAuthority('Quản lý chức năng hệ thống:Sửa')")
    public String doEditSF(@RequestParam Map<String, String> reqParam, Principal principal,
                             RedirectAttributes redir) {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            System.out.println(reqParamObj);
            boolean result =  systemFunctionalService.editSF(reqParamObj, principal);
            System.out.println(result);
            if (result) {
                insertLogService.insertLog(principal.getName(), "/vasonline/admin/system-functional", ConstantLog.EDIT,
                        principal.getName() + " edit sf success");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/admin/system-functional";
            }
            else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/admin/system-functional";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/admin/system-functional";
        }
    }

    @GetMapping(value = "/admin/system-functional/delete/{sfId}")
    @PreAuthorize("hasAnyAuthority('Quản lý chức năng hệ thống:Xoá')")
    public ModelAndView deleteSF(@PathVariable(value = "sfId") long id, Principal principal,RedirectAttributes reDir){
        try {
            String username = principal.getName();
            System.out.println(username);
            List<UsersLevelsFunctional> ulf =  usersLevelsFunctionalRepository.findSFByUsernameAndSFId(username,id);
            usersLevelsFunctionalRepository.deleteAll(ulf);

            List<UsersActionHistory> uah =  UahRepository.findSFByUsernameAndSFId(username,id);
            UahRepository.deleteAll(uah);

            systemFunctionalService.deleteSF(id);
            insertLogService.insertLog(principal.getName(),"/vasonline/admin/system-functional", ConstantLog.DELETE,
                    principal.getName()+" delete sf "+ id +" success");
            reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/admin/system-functional");
        } catch (Exception e) {
            reDir.addFlashAttribute("message",ConstantNotify.FAILED);
            return new ModelAndView("redirect:/admin/system-functional");
        }
    }

    @GetMapping(value = "/admin/system-functional/get-sfFather")
    @ResponseBody
    public List<String> getSfFather(@RequestParam Integer status){
        try {
            if (status != null){
                return systemFunctionalRepository.findSFNameByStatus(status);
            }else {
                return null;
            }
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

}
