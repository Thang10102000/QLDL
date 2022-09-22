/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.service.GroupService;
import com.neo.vas.service.InsertLogService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Controller
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private InsertLogService insertLogService;

	@GetMapping("/admin/group-management")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Xem')")
	public ModelAndView searchGroup() {
		return new ModelAndView("Groups/group-search");
	}

	@GetMapping("/admin/group-management/create")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Thêm')")
	public ModelAndView createGroup() {
		return new ModelAndView("Groups/group-create");
	}

	@GetMapping("/admin/group-management/edit/{id}")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Sửa')")
	public ModelAndView editGroup(@PathVariable("id") String id) {
		return new ModelAndView("Groups/group-edit", "groupData", groupService.findByGroupId(id));
	}

	@PostMapping("/admin/group-management/create")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Thêm')")
	public String saveGroup(@RequestParam Map<String, String> reqParam, Principal principal, RedirectAttributes reDir) throws SQLException {
		try {
			JSONObject data = new JSONObject(reqParam);
			String[] parts = data.getString("group-member").split(Pattern.quote(","));
			JSONArray groupMember = new JSONArray();
			for (String s : parts) {
				groupMember.put(s);
			}
			data.put("group-member", groupMember);
			String result = groupService.saveGroup(data, principal);
			if(result.equals("TRUE")){
				insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.CREATE,
						principal.getName() + " create group management success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return "redirect:/admin/group-management";
			}
			else if(result.equals("EXISTING_GROUP")) {
				reDir.addFlashAttribute("message", "EXISTING_GROUP");
				return "redirect:/admin/group-management";
			}
			else if(result.equals("FAILED")){
				reDir.addFlashAttribute("message", ConstantNotify.FAILED);
				return "redirect:/admin/group-management";
			}
			else
			{
				insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.CREATE,
						principal.getName() + " create group management success");
				reDir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				reDir.addFlashAttribute("message", result);
				return "redirect:/admin/group-management";
			}
		} catch (Exception e) {
			reDir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/admin/group-management";
		}
	}

	@PostMapping("/admin/group-management/edit")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Sửa')")
	public String editGroup(@RequestParam Map<String, String> reqParam, RedirectAttributes redir,
			Principal principal) throws SQLException {
		try {
			JSONObject data = new JSONObject(reqParam);
			String[] parts = data.getString("group-member").split(Pattern.quote(","));
			JSONArray groupMember = new JSONArray();
			for (String s : parts) {
				groupMember.put(s);
			}
			data.put("group-member", groupMember);
			String result = groupService.editGroup(data, principal);
			if(result.equals("TRUE")){
				insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.EDIT,
						principal.getName() + " edit group management " + data.getString("groupId") + " success");
				redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
				return "redirect:/admin/group-management/edit/" + reqParam.get("groupId");
			} 
			else if(result.equals("FALSE")){
				redir.addFlashAttribute("message", ConstantNotify.FAILED);
				return "redirect:/admin/group-management/edit/" + reqParam.get("groupId");
			} 
			else {
				insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.EDIT,
						principal.getName() + " edit group management " + data.getString("groupId") + " success");
				redir.addFlashAttribute("message", result);
				return "redirect:/admin/group-management/edit/" + reqParam.get("groupId");
			}
		} catch (Exception e) {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/admin/group-management/edit/" + reqParam.get("groupId");
		}
		
	}

	@PreAuthorize(value = "hasAnyAuthority('Quản lý nhóm tài khoản:Xoá')")
	@GetMapping("/admin/group-management/delete/{id}")
	public String deleteGroup(@PathVariable(value = "id") String id, Principal principal, RedirectAttributes redir) throws SQLException {
		if (groupService.deleteGroup(groupService.findByGroupId(id))) {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.DELETE,
					principal.getName() + " delete group management " + id + " success");
			redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			return "redirect:/admin/group-management";
		}
		else {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
			return "redirect:/admin/group-management";
		}
	}
}
