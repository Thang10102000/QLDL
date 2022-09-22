package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.*;
import com.neo.vas.repository.*;
import com.neo.vas.service.*;
import com.neo.vas.service.impl.AgencyServiceImpl;
import com.neo.vas.service.impl.CustomerServiceImpl;
import com.neo.vas.service.impl.SchoolServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AgencyAreaService agencyAreaService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private WardService wardService;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/customer")
    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Xem')")
    public ModelAndView getCustomer(Model model, Principal principal)
    {
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if(levelName.equals("MDS")){
            model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        }
        else if(levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("areas", agencyAreaService.getAgencyAreaById(areaId));
        }
        else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
            Long agencyId = user.getAgencyId();
            Agency agency = agencyService.getAgencyById(agencyId);
            if (agency != null){
                model.addAttribute("agency",agency);
                model.addAttribute("areas",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
            }else {
                model.addAttribute("agency",null);
                model.addAttribute("areas",null);
            }
        }
        else{
            model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        }
        model.addAttribute("school",schoolService.getAll());
        ModelAndView modelAndView = new ModelAndView("Customer/customer");
        modelAndView.addObject("page", 1);
        modelAndView.addObject("size", 5);
        return modelAndView;
    }

    @GetMapping("/new-cus")
    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Thêm')")
    public String newCus(Model model,Principal principal)
    {
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if(levelName.equals("MDS")){
            model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        }
        else if(levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("areas", agencyAreaService.getAgencyAreaById(areaId));
        }
        else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
            Long agencyId = user.getAgencyId();
            Agency agency = agencyService.getAgencyById(agencyId);
            if (agency != null){
                model.addAttribute("agency",agency);
                model.addAttribute("areas",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
            }else {
                model.addAttribute("agency",null);
                model.addAttribute("areas",null);
            }
        }
        else{
            model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        }

//        model.addAttribute("agency",agencyService.getAll());
//        model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        model.addAttribute("school", schoolService.getAll());
        model.addAttribute("listProvince", provinceService.getAllProvince());
//        model.addAttribute("listWard", wardService.getAllWard());
        return "Customer/new-customer";
    }

    @PostMapping("/new-cus")
    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Thêm')")
    public String createCus(@RequestParam Map<String, String> reParam, Principal principal, RedirectAttributes reDir)
    {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            boolean rs = customerService.createCustomer(jsonObject, principal);
            if (rs) {
                insertLogService.insertLog(principal.getName(), "/vasonline/customer", ConstantLog.CREATE,
                        principal.getName() + " create brand success");
                reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            } else {
                reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            }
            return "redirect:/customer";
        }catch (Exception e)
        {
            e.printStackTrace();
            return "redirect:/customer";
        }
    }

    @GetMapping(value = "/edit-cus/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Sửa')")
    public String editBrand(@PathVariable(value = "id") Long id, Model model, Principal principal) {
        Customer cus = customerService.getCusById(id);
        String username = principal.getName();
        Users user = usersRepository.findUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if(levelName.equals("MDS")){
            model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
            model.addAttribute("agency", agencyService.getListAgencyByArea(cus.getAreaCId().getAreaId()));
        }
        else if(levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("areas", agencyAreaService.getAgencyAreaById(areaId));
            model.addAttribute("agency", agencyService.getListAgencyByArea(areaId));
        }
        else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
            Long agencyId = user.getAgencyId();
            Agency agency = agencyService.getAgencyById(agencyId);
            if (agency != null){
                model.addAttribute("agency",agency);
                model.addAttribute("areas",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
            }else {
                model.addAttribute("agency",null);
                model.addAttribute("areas",null);
            }
        }
        else{
            model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        }
        model.addAttribute("cus", cus);
//        model.addAttribute("areas", agencyAreaService.getAllAgencyArea());
        model.addAttribute("school", schoolService.getAll());
        model.addAttribute("listProvince", provinceService.getAllProvince());
        if (!cus.getProvince().isEmpty()){
            model.addAttribute("listDistrict", districtService.getDistrictByProvinceId(Long.parseLong(cus.getProvince())));
        }else {
            model.addAttribute("listDistrict", null);
        }
        if (!cus.getWards().isEmpty()){
            model.addAttribute("listWards", wardService.getWardByDistrictId(Long.parseLong(cus.getDistrict())));
        }else {
            model.addAttribute("listWards", null);
        }
//        if (cus.getAreaCId() != null){
//            model.addAttribute("agency", agencyService.getListAgencyByArea(cus.getAreaCId().getAreaId()));
//        }else {
//            model.addAttribute("agency", null);
//        }
        return "Customer/edit-customer";
    }

    @PostMapping(value = "/edit-cus")
    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Sửa')")
    public ModelAndView updateBrand(@RequestParam Map<String, String> reParam, Principal principal,RedirectAttributes reDir) {
        try {
            System.out.println(reParam);
            JSONObject jsonParam = new JSONObject(reParam);
            System.out.println(jsonParam);
            boolean rs = customerService.saveCustomer(jsonParam, principal);
            if (rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/customer", ConstantLog.EDIT,
                        principal.getName()+" edit customer "+ jsonParam.getString("id") +" success");
                reDir.addFlashAttribute("message",ConstantNotify.SUCCESS);
                return new ModelAndView("redirect:/customer");
            }else {
                reDir.addFlashAttribute("message",ConstantNotify.FAILED);
                return new ModelAndView("redirect:/customer");
            }
        } catch (Exception e) {
            System.err.println(e);
            reDir.addFlashAttribute("message",ConstantNotify.FAILED);
            return new ModelAndView("redirect:/customer");
        }
    }

    @GetMapping(value = "/delete-cus/{id}")
    @PreAuthorize("hasAnyAuthority('Quản lý khách hàng:Xoá')")
    public ModelAndView deleteCus(@PathVariable(value = "id") long id, Principal principal, RedirectAttributes reDir) {
        try {
            customerService.deleteById(id);
            insertLogService.insertLog(principal.getName(), "/vasonline/customer", ConstantLog.DELETE,
                    principal.getName() + " delete customer " + id + " success");
            reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
            return new ModelAndView("redirect:/customer");
        } catch (Exception e) {
            reDir.addFlashAttribute("message", ConstantNotify.FAILED);
            return new ModelAndView("redirect:/customer");
        }
    }

    @GetMapping(value = "/get-customer-area")
    @ResponseBody
    public List<Customer> getCustomerByAreaId(@RequestParam Long areaId) {
        try {
            return customerService.getCustomerByAreaId(areaId);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    @GetMapping(value = "/get-allcustomer")
    @ResponseBody
    public List<Customer> getAllCustomer() {
        try {
            return customerService.getAllCustomer();
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

//    lay tinh huyen xa
    @GetMapping(value = "/get-district")
    @ResponseBody
    public List<District> getDistrictByProvince(@RequestParam String provinceId){
        try {
            if (provinceId != null){
                return districtService.getDistrictByProvinceId(Long.parseLong(provinceId));
            }else {
                return null;
            }
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }

    @GetMapping(value = "/get-ward")
    @ResponseBody
    public List<Ward> getWardByDistrict(@RequestParam String districtId){
        try {
            if (districtId != null){
                return wardService.getWardByDistrictId(Long.parseLong(districtId));
            }else {
                return null;
            }
        }catch (Exception e){
            System.err.println(e);
            return null;
        }
    }
}
