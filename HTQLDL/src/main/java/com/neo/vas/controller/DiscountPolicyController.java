package com.neo.vas.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.DiscountCommission;
import com.neo.vas.domain.LimitDiscount;
import com.neo.vas.repository.DiscountCommissionRepository;
import com.neo.vas.repository.LimitDiscountRepository;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.DiscountPolicyService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.LimitDiscountService;
import com.neo.vas.service.impl.DiscountPolicyServiceImpl;
import com.neo.vas.service.impl.LimitDiscountServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.neo.vas.domain.Agency;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * project_name: demo Created_by: thulv time: 19/05/2021
 */
@Controller
public class DiscountPolicyController {
    @Autowired
    private DiscountPolicyService discountPolicyService;

    @Autowired
    private AgencyAreaService agencyAreaService;

    @Autowired
    private LimitDiscountService limitDiscountService;

    @Autowired
    private InsertLogService insertLogService;

    @GetMapping("/discount")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Xem')")
    public ModelAndView getAllDiscount() {
        ModelAndView modelAndView = new ModelAndView("Discount/discount_policy");
        modelAndView.addObject("page", 1);
        modelAndView.addObject("size", 5);
        return modelAndView;
    }

    @GetMapping("/new-discount")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Thêm')")
    public String newDiscount(Model model) {
        model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        return "Discount/new_discount";
    }

    @GetMapping("/edit-discount/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Sửa')")
    public String editDiscount(@PathVariable(value = "id") Long id, Model model) {
        DiscountCommission discountPolicy = discountPolicyService.getDiscountPolicyById(id);
        model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        model.addAttribute("limitDiscount", limitDiscountService.getLimitByDiscount(id));
        model.addAttribute("discounts", discountPolicy);
        return "Discount/edit_discount";
    }

    @GetMapping("/delete-discount/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Xoá')")
    public ModelAndView deleteDiscount(@PathVariable(value = "id") Long id, Principal principal, RedirectAttributes reDir){
        try {
            DiscountCommission coP = discountPolicyService.getDiscountPolicyById(id);
            Date date = new Date();
            if (coP.getStartDate().before(date) && coP.getEndDate().after(date)) {
                reDir.addFlashAttribute("errDelete", "Chính sách chiết khấu đang còn hiệu lực");
                return new ModelAndView("redirect:/discount");
            }
            this.discountPolicyService.deleteDiscountPolicyById(id);
            insertLogService.insertLog(principal.getName(), "/vasonline/discount", ConstantLog.DELETE,
                    principal.getName() + " delete discount policy success");
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/discount");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/discount");
        }

    }

    @PostMapping("/new-discount")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Thêm')")
    public ModelAndView createDiscount(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            String[] parts = jsonObject.getString("agency").split(Pattern.quote(","));
            JSONArray jsonArray = new JSONArray();
            if (parts[0] != "") {
                for (String s : parts) {
                    jsonArray.put(s);
                }
            }
            jsonObject.put("agency", jsonArray);
            System.out.println("data chinh sach chiet khau: " + jsonObject);

            //			check trùng chính sách chiết khấu mặc định
            if (!jsonObject.getString("startDate").isEmpty() && !jsonObject.getString("endDate").isEmpty()) {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonObject.getString("startDate"));
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(jsonObject.getString("endDate"));
                DiscountCommission checkIsDefault = discountPolicyService.getPolicyIsDefault(startDate, endDate, 1);
                if (!jsonObject.getString("checkDefault").isEmpty() && checkIsDefault != null) {
                    reDir.addFlashAttribute("error", "Chính sách mặc định tại thời điểm hiện tại đã có");
                    return new ModelAndView("redirect:/discount");
                }
            }

            boolean rs = discountPolicyService.createDiscountPolicy(jsonObject, principal);
            if (rs) {
                insertLogService.insertLog(principal.getName(), "/vasonline/discount", ConstantLog.CREATE,
                        principal.getName() + " create discount policy success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/discount");
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/discount");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/discount");
        }
    }

    @GetMapping(value = "/agency-list")
    @ResponseBody
    public List<Agency> listAgency(Model model, @RequestParam String idAgencyArea) {
        List<Agency> list = discountPolicyService.getListAgency(idAgencyArea);
        model.addAttribute("brandList", list);
        return list;
    }

    @GetMapping(value = "/agency-list-no-policy")
    @ResponseBody
    public List<Agency> listAgencyNoPolicy(Model model, @RequestParam String idAgencyArea) {
        List<Agency> list = discountPolicyService.getListAgencyNoPolicy(idAgencyArea);
        model.addAttribute("brandList", list);
        return list;
    }

    @GetMapping(value = "/agencyamkam-list")
    @ResponseBody
    public List<Agency> listAgencyAmkam(Model model, @RequestParam String idAgencyArea) {
        List<Agency> list = discountPolicyService.getListAgencyAmkam(idAgencyArea);
        model.addAttribute("brandList", list);
        return list;
    }

    @PostMapping("/edit-discount")
    @PreAuthorize("hasAnyAuthority('Quản lý chính sách chiết khấu:Sửa')")
    public ModelAndView updateDiscount(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            String[] parts = jsonObject.getString("totalIdLimit").split(Pattern.quote(","));
            JSONArray jsonArray = new JSONArray();
            if (parts[0] != "") {
                for (String s : parts) {
                    jsonArray.put(s);
                }
            }
            jsonObject.put("totalIdLimit", jsonArray);
            System.out.println(jsonObject);
            boolean rs = discountPolicyService.updateDiscountPolicy(jsonObject, principal);
            if (rs) {
                insertLogService.insertLog(principal.getName(), "/vasonline/discount", ConstantLog.EDIT,
                        principal.getName() + " edit discount policy " + jsonObject.getString("id") + " success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/discount");
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
                return new ModelAndView("redirect:/discount");
            }
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/discount");
        }
    }

    //get rate for default rate brand
    @GetMapping(value = "/default-discountRate")
    @ResponseBody
    public LimitDiscount apiSupportBrand() {
        Date now = new Date();
        DiscountCommission checkIsDefault = discountPolicyService.findDiscountEffect(now);
        LimitDiscount limitDiscount = limitDiscountService.findLimitDiscountByDiscountCommission(checkIsDefault);
        return limitDiscount;
    }
}
