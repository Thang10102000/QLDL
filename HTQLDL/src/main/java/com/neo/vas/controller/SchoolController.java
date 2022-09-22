package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.School;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.SchoolService;
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

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private InsertLogService insertLogService;

    @GetMapping(value = "/school")
    @PreAuthorize("hasAnyAuthority('Quản lý loại trường học:Xem')")
    public ModelAndView school() {
        ModelAndView modelAndView = new ModelAndView("School/school");
        modelAndView.addObject("page", 1);
        modelAndView.addObject("size", 5);
        return modelAndView;
    }

    @GetMapping(value = "/add-school")
    @PreAuthorize("hasAnyAuthority('Quản lý trường học:Thêm')")
    public String newSchool() {
        return "School/add_school";
    }

    @PostMapping(value = "/create-school")
    @PreAuthorize("hasAnyAuthority('Quản lý trường học:Thêm')")
    public String createSchool(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            if (!jsonObject.getString("schoolCode").isEmpty()) {
                School checkCode = schoolService.findSchoolBySchoolCode(jsonObject.getString("schoolCode").trim());
                if (checkCode != null){
                    reDir.addFlashAttribute("error",ConstantNotify.FAILED);
                    return "redirect:/school";
                }
            }
            boolean rs = schoolService.createSchool(jsonObject, principal);
            if (rs) {
                insertLogService.insertLog(principal.getName(), "/vasonline/school", ConstantLog.CREATE,
                        principal.getName() + " create service success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            }
            return "redirect:/school";
        } catch (Exception e) {
            return "redirect:/school";
        }
    }

    @GetMapping(value = "/edit-school/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý trường học:Sửa')")
    public String editSchool(@PathVariable(value = "id") Long id, Model model) {
        School school = schoolService.getSchoolById(id);
        model.addAttribute("school", school);
        return "School/edit_school";
    }

    @PostMapping(value = "/edit-school")
    @PreAuthorize("hasAnyAuthority('Quản lý trường học:Sửa')")
    public String editSchool(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            System.out.println(jsonObject);
            boolean rs = schoolService.editSchool(jsonObject, principal);
            String description = "";
            if (rs) {
                description = principal.getName() + " edit service " + jsonObject.getString("id") + " success";
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            }else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            }
            return "redirect:/school";
        } catch (Exception e) {
            System.err.println("Lỗi >>>> " + e);
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/school";
        }
    }

    @GetMapping(value = "/delete-school/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý trường học:Xoá')")
    public String deleteSchool(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir) {
        try {
            this.schoolService.deleteSchool(id);
            insertLogService.insertLog(principal.getName(), "/vasonline/school", ConstantLog.DELETE,
                    principal.getName() + " delete service " + id + " success");
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return "redirect:/school";
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/school";
        }
    }
}
