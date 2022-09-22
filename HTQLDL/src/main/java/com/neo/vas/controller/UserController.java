/**
 *
 */
package com.neo.vas.controller;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.domain.*;
import com.neo.vas.repository.GroupsUsersRepository;
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

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Controller
public class UserController {
    public static final Integer LEVEL_ID_CTKV = 2;
    @Autowired
    private UsersService usersService;
    @Autowired
    private InsertLogService insertLogService;
    @Autowired
    private GroupsUsersRepository guRepository;
    @Autowired
    private GroupService groupService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AgencyAreaService agencyAreaService;

    @GetMapping("/admin/user-management")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Xem')")
    public ModelAndView searchUser(Principal principal, Model model, RedirectAttributes reDir) {
        String username = principal.getName();
        Users user = usersService.getUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            reDir.addFlashAttribute("levelUser", LEVEL_ID_CTKV);
            Long areaId = user.getAreaId().getAreaId();
            model.addAttribute("myArea", agencyAreaService.getAgencyAreaById(areaId));
        }
//		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
//			Long agencyId = user.getAgencyId();
//			Agency agency = agencyService.getAgencyById(agencyId);
//			if (agency != null){
//				model.addAttribute("agency",agency);
//				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
//			}else {
//				model.addAttribute("agency",null);
//				model.addAttribute("agencyArea",null);
//			}
//		}
        else {
            model.addAttribute("myArea", agencyAreaService.getAllAgencyArea());
        }
        return new ModelAndView("Users/user-search");
    }

    @GetMapping("/admin/user-management/create")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Thêm')")
    public ModelAndView getUserCreate(Principal principal, Model model) {
        String username = principal.getName();
        Users user = usersService.getUsersByUsername(username);
        String levelName = user.getLevelUsers().getLevelName();
        if (levelName.equals("MDS")) {
//			reDir.addFlashAttribute("level",1);
            model.addAttribute("agencyArea", agencyAreaService.getAllAgencyArea());
        } else if (levelName.equals("Công ty khu vực")) {
            Long areaId = user.getAreaId().getAreaId();

            model.addAttribute("myArea", agencyAreaService.getAgencyAreaById(areaId));
        }
//		else if(levelName.equals("Đại lý") || levelName.equals("AM/KAM")){
//			Long agencyId = user.getAgencyId();
//			Agency agency = agencyService.getAgencyById(agencyId);
//			if (agency != null){
//				model.addAttribute("agency",agency);
//				model.addAttribute("agencyArea",agencyAreaService.getAgencyAreaById(agency.getAreaId().getAreaId()));
//			}else {
//				model.addAttribute("agency",null);
//				model.addAttribute("agencyArea",null);
//			}
//		}
        else {
            model.addAttribute("myArea", agencyAreaService.getAllAgencyArea());
        }
        return new ModelAndView("Users/user-create");
    }

    @GetMapping("/admin/user-management/edit/{username}")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Sửa')")
    public ModelAndView getUserEdit(@PathVariable("username") String username, Model model) {
        Users user = usersService.getUsersByUsername(username);
        Levels level = user.getLevelUsers();
        List<Groupss> grList = groupService.getGroupByLevel(level.getLevelId());
        List<GroupsUsers> guList = guRepository.findByUsersGU(user);
        AgencyArea aa = user.getAreaId();
        List<AgencyArea> aaList = new ArrayList<AgencyArea>();
        aaList.add(aa);
        List<Agency> agencyList = agencyService.getAgencyByArea(aaList);
        Groupss group = new Groupss();
        for (GroupsUsers g : guList) {
            group = g.getGroupIdGU();
        }
        model.addAttribute("groupListUser", grList);
        model.addAttribute("groupUser", group);
        model.addAttribute("agencyListUser", agencyList);
        return new ModelAndView("Users/user-edit", "userData", usersService.getUsersByUsername(username));
    }

    @PostMapping("/admin/user-management/create")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Thêm')")
    public String doCreateUser(@RequestParam Map<String, String> reqParam, Principal principal, Model model,
                               RedirectAttributes redir) throws SQLException {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            String result = usersService.saveNewUser(reqParamObj, principal);
            if (result.equals("TRUE")) {
                insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.CREATE,
                        principal.getName() + " create user success");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/admin/user-management";
            } else if (result.equals("EXISTING_ACC")) {
                redir.addFlashAttribute("message", "EXISTING_ACC");
                return "redirect:/admin/user-management";
            } else if (result.equals("WRONG_PASSWORD")) {
                redir.addFlashAttribute("message", "WRONG_PASSWORD");
                return "redirect:/admin/user-management";
            } else if (result.equals("ExistUserInAgency")) {
                redir.addFlashAttribute("message", "ExistUserInAgency");
                return "redirect:/admin/user-management";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/admin/user-management";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/admin/user-management";
        }
    }

    @PostMapping("/admin/user-management/edit")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Sửa')")
    public String doEditUser(@RequestParam Map<String, String> reqParam, Principal principal,
                             RedirectAttributes redir) {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            String result = usersService.editUser(reqParamObj, principal);
            if (result.equals("TRUE")) {
                insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.EDIT,
                        principal.getName() + " edit user success");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/admin/user-management";
            } else if (result.equals("WRONG_PASSWORD")) {
                redir.addFlashAttribute("message", "WRONG_PASSWORD");
                return "redirect:/admin/user-management";
            } else if (result.equals("INVALID_ACC")) {
                redir.addFlashAttribute("message", "INVALID_ACC");
                return "redirect:/admin/user-management";
            } else if (result.equals("ExistUserInAgency")) {
                redir.addFlashAttribute("message", "ExistUserInAgency");
                return "redirect:/admin/user-management";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/admin/user-management";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/admin/user-management";
        }
    }

    @GetMapping("/admin/user-management/delete/{username}")
    @PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Xoá')")
    public String doDeleteUser(@PathVariable("username") String username, Principal principal, RedirectAttributes redir)
            throws SQLException {
        try {
            if (usersService.deleteUser(username, principal)) {
                insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.DELETE,
                        principal.getName() + " delete user success");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/admin/user-management";
            }
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/admin/user-management";
        } catch (Exception e) {
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/admin/user-management";
        }
    }

    @GetMapping("/admin/user-management/changePassword")
    public ModelAndView getChangePass() {
        return new ModelAndView("fragments/user-change-pass");
    }

    @PostMapping("/admin/user-management/changePassword")
    public String doChangPassword(@RequestParam Map<String, String> reqParam, Principal principal,
                                  RedirectAttributes redir) {
        try {
            JSONObject reqParamObj = new JSONObject(reqParam);
            String result = usersService.changePassword(reqParamObj, principal);
            if (result.equals("TRUE")) {
                insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.EDIT,
                        principal.getName() + " change password success");
                redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
                return "redirect:/dashboard";
            } else if (result.equals("WRONG_PASSWORD")) {
                redir.addFlashAttribute("message", "WRONG_PASSWORD");
                return "redirect:/dashboard";
            } else if (result.equals("WRONG_OLD_PASS")) {
                redir.addFlashAttribute("message", "WRONG_OLD_PASS");
                return "redirect:/dashboard";
            } else {
                redir.addFlashAttribute("message", ConstantNotify.FAILED);
                return "redirect:/dashboard";
            }
        } catch (Exception e) {
            System.err.println(e);
            redir.addFlashAttribute("message", ConstantNotify.FAILED);
            return "redirect:/dashboard";
        }
    }
}
