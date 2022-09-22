package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.AgencyArea;
import com.neo.vas.repository.AgencyAreaRepository;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.InsertLogService;
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
import java.util.Map;

@Controller
public class AgencyAreaController {
    @Autowired
    AgencyAreaService agencyAreaService;
    @Autowired
    private InsertLogService insertLogService;

    @GetMapping(value = "/area")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Xem')")
    public ModelAndView getArea(Model model) {
        ModelAndView modelAndView = new ModelAndView("AgencyArea/agency_area");
        modelAndView.addObject("page", 1);
        modelAndView.addObject("size", 5);
        return modelAndView;
    }


    @GetMapping(value = "/new-area")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Thêm')")
    public String newArea(Model model) {
        return "AgencyArea/new-area";
    }

    @PostMapping(value = "/new-area")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Thêm')")
    public String createArea(@RequestParam Map<String,String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);

            if (!jsonObject.getString("areaCode").isEmpty()) {
                AgencyArea checkCode = agencyAreaService.getAgencyAreaByCode(jsonObject.getString("areaCode"));
                if(checkCode != null ){
                    reDir.addFlashAttribute("error",ConstantNotify.FAILED);
                    return "redirect:/area";
                }
            }
            boolean rs = agencyAreaService.createAgencyArea(jsonObject,principal);
            if (rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/area", ConstantLog.CREATE,
                        principal.getName()+" create brand success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                System.out.println("ok");
                return"redirect:/area";
            }else {
                insertLogService.insertLog(principal.getName(),"/vasonline/area", ConstantLog.CREATE,
                        principal.getName()+" create brand failed");
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                System.out.println("fail");
                return "redirect:/area";
            }
        } catch (Exception e) {
            System.err.println(e);
            return "redirect:/area";
        }
    }

    @GetMapping(value = "/edit-area/{areaId}")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Sửa')")
    public String editArea(@PathVariable(value = "areaId") long id, Model model) {
        AgencyArea agencyArea = agencyAreaService.getAgencyAreaById(id);
        model.addAttribute("area",agencyArea);
        return "AgencyArea/edit-area";
    }

    @PostMapping(value = "/edit-area")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Sửa')")
    public String editArea(@RequestParam Map<String,String> reParam, Principal principal, RedirectAttributes reDir) {

        try {
            JSONObject jsonObject = new JSONObject(reParam);
            boolean rs = agencyAreaService.saveAgencyArea(jsonObject,principal);
            if (rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/area", ConstantLog.CREATE,
                        principal.getName()+" create brand success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return"redirect:/area";
            }else {
                insertLogService.insertLog(principal.getName(),"/vasonline/area", ConstantLog.CREATE,
                        principal.getName()+" create brand failed");
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/area";
            }
        } catch (Exception e) {
            System.err.println(e);
            return "redirect:/area";
        }
    }

    @GetMapping(value = "/delete-area/{areaId}")
    @PreAuthorize("hasAnyAuthority('Quản lý CTKV - Trung tâm:Xoá')")
    public ModelAndView deleteArea(@PathVariable(value = "areaId") long id, Principal principal,RedirectAttributes reDir){
        try {
            agencyAreaService.deleteAgencyArea(id);
            insertLogService.insertLog(principal.getName(),"/vasonline/area", ConstantLog.DELETE,
                    principal.getName()+" delete brand "+ id +" success");
            reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/area");
        } catch (Exception e) {
            reDir.addFlashAttribute("message",ConstantNotify.FAILED);
            return new ModelAndView("redirect:/area");
        }
    }

}
