/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.constant.ConstantNotify;
import com.neo.vas.service.GroupService;
import com.neo.vas.service.GroupUserService;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.UsersService;
import com.neo.vas.service.impl.GroupServiceImpl;
import com.neo.vas.service.impl.UsersServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.repository.GroupsRepository;
import com.neo.vas.repository.GroupsUsersRepository;
import com.neo.vas.repository.UsersRepository;
import com.neo.vas.service.impl.GroupUserServiceImpl;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@RestController
public class RestGroupUserController {
	@Autowired
	private GroupUserService groupUserService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private InsertLogService insertLogService;

	@PreAuthorize(value = "hasAnyAuthority('Quản lý nhóm tài khoản:Xem')")
	@GetMapping("/admin/user/group")
	public Map<JSONObject, Integer> doSearchUser(@RequestParam String username, @RequestParam String groupId,
												 @RequestParam(name = "page", required = false, defaultValue = "0") int page,
												 @RequestParam(name = "size", required = false, defaultValue = "5") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		HashMap<JSONObject, Integer> data = new HashMap<>();
		try {
			insertLogService.insertLog(principal.getName(),"/vasonline/admin/group-management", ConstantLog.SEARCH,
					principal.getName()+" search group user");
			GroupsUsers groupsUsers = new GroupsUsers();
			if (groupId.isEmpty()) {
				return data;
			} else {
				groupsUsers.setGroupIdGU(groupService.findByGroupId(groupId));
				groupsUsers.setUsersGU((!username.isEmpty()) ? usersService.getUsersByUsername(username) : null);
				Page<GroupsUsers> pages = groupUserService.searchGroupUsers(groupsUsers, realPage, size);
				for (GroupsUsers gu : pages) {
					JSONObject gudata = gu.createJson();
					data.put(gudata, pages.getTotalPages());
				}
				return data;
			}
		} catch (Exception e) {
			System.err.println(e);
			return data;
		}
	}

	@PreAuthorize(value = "hasAnyAuthority('Quản lý nhóm tài khoản:Xoá')")
	@GetMapping("/admin/user/remove/{id}")
	public RedirectView deleteUserFromGroup(@PathVariable(value = "id") Long id, RedirectAttributes redir, Principal principal) {
		GroupsUsers gu = groupUserService.findByGuId(id);
		RedirectView rView = new RedirectView("/admin/group-management/edit/" + gu.getGroupIdGU().getGroupId(), true);
		try {
			if (groupUserService.deleteUserFromGroup(gu)) {
				insertLogService.insertLog(principal.getName(),"/vasonline/admin/group-management", ConstantLog.DELETE,
						principal.getName()+" delete group user "+id+" success");
				redir.addFlashAttribute("message", ConstantNotify.SUCCESS);
			} else
				redir.addFlashAttribute("message", ConstantNotify.FAILED);
		} catch (Exception e) {
			redir.addFlashAttribute("message", ConstantNotify.FAILED);
		}
		return rView;
	}
}
