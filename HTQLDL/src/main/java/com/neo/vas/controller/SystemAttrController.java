package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.SystemAttr;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SystemAttrService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class SystemAttrController {
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private SystemAttrService systemAttrService;

    @GetMapping(value = {"/system-attr"})
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Xem')")
    public ModelAndView listSystemAttr(Model model, Principal principal) {
        return new ModelAndView("/SystemAttr/search");
    }

    @GetMapping("/system-attr/search")
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Xem')")
    public Map<JSONObject, Integer> searchSystemAttr(@RequestParam String saId, String type, String name, String value, String description,
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
            insertLogService.insertLog(principal.getName(), "/vasonline/system-attr", ConstantLog.SEARCH,
                    principal.getName() + " search system attr");
            Page<SystemAttr> pageSystemAttr = systemAttrService.searchSystemAttr(saId, type, name, value, description, realPage, size);
            for (SystemAttr ag : pageSystemAttr) {
                JSONObject js = ag.createJson();
                data.put(js, pageSystemAttr.getTotalPages());
            }
            return data;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    @GetMapping("/system-attr/create")
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Thêm')")
    public ModelAndView newSystemAttr(Model model,Principal principal)
    {
        return new ModelAndView("/SystemAttr/create");
    }

    @PostMapping("/system-attr/create")
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Thêm')")
    public ModelAndView createSystemAttr(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir)
    {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            boolean rs = systemAttrService.createSystemAttr(jsonObject, principal);
            if (rs) {
                insertLogService.insertLog(principal.getName(), "/vasonline/system-attr/create", ConstantLog.CREATE,
                        principal.getName() + " create system attr");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            }
            return new ModelAndView("redirect:/system-attr");
        }catch (Exception e)
        {
            e.printStackTrace();
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/system-attr");
        }
    }
    @GetMapping(value = "/system-attr/delete/{id}")
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Xoá')")
    public ModelAndView deleteSystemAttr(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir) {
        try {
            systemAttrService.deleteById(id);
            insertLogService.insertLog(principal.getName(), "/vasonline/customer", ConstantLog.DELETE,
                    principal.getName() + " delete system attr " + id + " success");
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/system-attr");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/system-attr");
        }
    }

    @PostMapping(value = "/system-attr/edit")
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Sửa')")
    public ModelAndView updateBrand(@RequestParam Map<String, String> reParam, Principal principal,RedirectAttributes reDir) {
        try {
            JSONObject jsonParam = new JSONObject(reParam);
            boolean rs = systemAttrService.editSystemAttr(jsonParam, principal);
            if (rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/system-attr/edit", ConstantLog.EDIT,
                        principal.getName()+" edit system attr "+ jsonParam.getString("saId") +" success");
                reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/system-attr");
            }else {
                reDir.addFlashAttribute("message",ConstantNotify.FAILED);
                return new ModelAndView("redirect:/system-attr");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reDir.addFlashAttribute("message",ConstantNotify.FAILED);
            return new ModelAndView("redirect:/system-attr");
        }
    }

    @GetMapping(value = "/system-attr/edit/{id}")
    @PreAuthorize("hasAnyAuthority('Danh mục tham số hệ thống:Sửa')")
    public ModelAndView editSystemAttr(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        SystemAttr systemAttr = systemAttrService.getSaById(id);
        model.addAttribute("systemAttr", systemAttr);
        return new ModelAndView("/SystemAttr/edit");
    }
}
