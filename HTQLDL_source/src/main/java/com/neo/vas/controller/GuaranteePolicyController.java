package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.service.GuaranteePolicyService;
import com.neo.vas.service.InsertLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.sql.SQLException;
/**
 * @author hai
 *
 */
@Controller
public class GuaranteePolicyController {

    @Autowired
    private GuaranteePolicyService gpsService;

    @Autowired
    private InsertLogService insertLogService;

    @GetMapping("/guarantee-policy")
    @PreAuthorize(value = "hasAnyAuthority('Quản lý chính sách bảo lãnh:Xem')")
    public ModelAndView getGuaranteePolicy() {
        ModelAndView mav = new ModelAndView("GuaranteePolicy/guarantee_policy");
        mav.addObject("limit", gpsService.getGuaranteePolicy(Long.parseLong("1")));
        return mav;
    }
    @PostMapping(value = "/edit-guarantee-policy")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách bảo lãnh:Sửa')")
    public ModelAndView updateBrand(@RequestParam String limit, Principal principal, RedirectAttributes reDir) {
        ModelAndView mav = new ModelAndView("redirect:/guarantee-policy");
        try {
            if (Integer.parseInt(limit) < 1 && Integer.parseInt(limit) > 100) {
                limit = "1";
            }
            if (gpsService.saveGuaranteePolicy(Integer.parseInt(limit))) {

                insertLogService.insertLog(principal.getName(), "/vasonline/guarantee-policy", ConstantLog.EDIT,
                        principal.getName() + " save config system success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            } else {
                reDir.addFlashAttribute("message",ConstantNotify.FAILED);
            }
        }catch (SQLException throwables) {
                throwables.printStackTrace();
        }
        return mav;

    }
}
