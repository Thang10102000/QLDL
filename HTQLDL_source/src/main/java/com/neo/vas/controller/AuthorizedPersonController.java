package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.Agency;
import com.neo.vas.domain.Users;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.AgencyAreaService;
import com.neo.vas.service.AgencyService;
import com.neo.vas.service.AuthorizedPersonService;
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

import java.security.Principal;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * project_name: demo
 * Created_by: thulv
 * time: 27/05/2021
 */
@Controller
public class AuthorizedPersonController {
    @Autowired
    private AuthorizedPersonService authorizedPersonService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private AgencyAreaService agencyAreaService;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/authorized")
    @PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Xem')")
    public ModelAndView getAllAuthorized(Model model, Principal principal) {
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
        ModelAndView mv = new ModelAndView("Agency/authorized_person");
        mv.addObject("page",1);
        mv.addObject("size",5);
        return mv;
    }

    @GetMapping("/new-authorized-person")
    @PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Thêm')")
    public ModelAndView createAuthor(Model model){
        model.addAttribute("agency", agencyService.getAllAgency());
        return new ModelAndView("Agency/new_authorized_person");
    }

    //    tạo mới
    @PostMapping("/new-authorized")
    @PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Thêm')")
    public ModelAndView newAuthorAgency(@RequestParam Map<String, String> reParam, Principal principal) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            boolean rs = authorizedPersonService.createAuthor(jsonObject);
            if(rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/authorized", ConstantLog.CREATE,
                        principal.getName()+" create authorized person success");
                return new ModelAndView("redirect:/authorized", "create", "Thêm mới thông tin liên hệ thành công");
            }else {
                return new ModelAndView("redirect:/authorized", "create", "Thêm mới thông tin liên hệ thất bại");
            }
        } catch (Exception e) {
            return new ModelAndView("redirect:/authorized", "create", "Thêm mới thông tin liên hệ thất bại");
        }
    }

    //    chỉnh sửa
    @GetMapping("/edit-authorized/{id}")
    @PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Sửa')")
    public ModelAndView editAuthorized(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("agency", agencyService.getAllAgency());
        return new ModelAndView("Agency/edit_authorized_person", "editAuthorized", authorizedPersonService.getAuthorPersonById(id));
    }

    @PostMapping("/edit-authorized")
    @PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Sửa')")
    public ModelAndView updateAuthorized(@RequestParam Map<String, String> reParam, Principal principal) {
        try {
            JSONObject jsonObject = new JSONObject(reParam);
            boolean rs = authorizedPersonService.updateAuthor(jsonObject);
            if(rs){
                insertLogService.insertLog(principal.getName(),"/vasonline/authorized", ConstantLog.EDIT,
                        principal.getName()+" edit authorized person "+ jsonObject.getLong("id") + " success");
                return new ModelAndView("redirect:/authorized", "edit", "Cập nhập thành công");
            }else {
                return new ModelAndView("redirect:/authorized", "edit", "Cập nhập thất bại");
            }
        } catch (Exception e) {
            return new ModelAndView("redirect:/authorized", "edit", "Cập nhập thất bại");
        }
    }

    //    Xoá
    @GetMapping("/delete-authorized/{id}")
    @PreAuthorize("hasAnyAuthority('Thông tin liên hệ đại lý:Xoá')")
    public ModelAndView deleteAuthor(@PathVariable(value = "id") Long id, Principal principal){
        try {
            this.authorizedPersonService.deleteAuthorPerson(id);
            insertLogService.insertLog(principal.getName(),"/vasonline/authorized", ConstantLog.DELETE,
                    principal.getName()+" delete authorized person success");
            return new ModelAndView("redirect:/authorized", "delete", "Xoá thành công");
        } catch (Exception e) {
            return new ModelAndView("redirect:/authorized", "delete", "Xoá thất bại");
        }
    }
}
