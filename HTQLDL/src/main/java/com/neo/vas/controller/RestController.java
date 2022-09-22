/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.domain.*;
import com.neo.vas.service.*;
import com.neo.vas.service.impl.GroupsLevelsFunctionalServiceImpl;
import com.neo.vas.service.impl.LevelsServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private UserLevelFunctionalService ulfService;
	@Autowired
	private GroupsLevelsFunctionalService glfService;
	@Autowired
	private GroupService gsService;
	@Autowired
	private LevelsService levelsService;
	@Autowired
	private InsertLogService insertLogService;
	@Autowired
	private SystemFunctionalService systemFunctionalService;

	@GetMapping("/admin/load/user")
	@PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Xem')")
	public Map<String, String> loadUsers(@RequestParam String username,
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "5") Integer size, Principal principal) {
		HashMap<String, String> data = new HashMap<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.SEARCH,
					principal.getName() + " search user");
			Page<Users> loaded = usersService.loadUsers(username, page, size);
			for (Users users : loaded) {
				data.put(users.getUsername(), "username");
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	@GetMapping("/admin/get/privileges/{username}")
	@PreAuthorize("hasAnyAuthority('Quản lý tài khoản:Thực thi')")
	public Map<String, String> loadUserPrivileges(@PathVariable String username, Principal principal) {
		HashMap<String, String> data = new HashMap<>();
		Set<String> setList = new HashSet<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.SEARCH,
					principal.getName() + " search privilege user");
			List<UsersLevelsFunctional> listULF = ulfService.findByUsersname(username);
			for (UsersLevelsFunctional ulf : listULF) {
				setList.add(ulf.getSystemFunctionalULF().getSfName() + ":" + ulf.getPrivilegesULF().getPrivilegeName());
			}
			List<GroupsLevelsFunctional> listGLF = glfService.findByUsersname(username);
			for (GroupsLevelsFunctional glf : listGLF) {
				setList.add(glf.getSystemFunctionalGLF().getSfName() + ":" + glf.getPrivilegesGLF().getPrivilegeName());
			}
			for (String item : setList) {
				data.put(item, username);
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return data;
	}

	@GetMapping("/admin/group/search")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Xem')")
	public Map<JSONObject, Integer> searchGroup(@RequestParam String groupId, @RequestParam String groupName,
			@RequestParam String groupFather,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
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
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.SEARCH,
					principal.getName() + " search group user");
			Groupss group = new Groupss();
			group.setGroupId(groupId);
			group.setGroupName(groupName);
			group.setGroupFather(gsService.findByGroupId(groupFather));
			Page<Groupss> pages = gsService.searchGroup(group, realPage, size);
			for (Groupss groupss : pages) {
				JSONObject gdata = groupss.createJson();
				data.put(gdata, pages.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return data;
		}
	}

	@GetMapping("/admin/system-functional/search")
	@PreAuthorize("hasAnyAuthority('Quản lý chính sách hệ thống:Xem')")
	public Map<JSONObject, Integer> searchSystemFunctional(@RequestParam String sfId, String sfName, String urlController, String sfFather, String sfStatus,
														   @RequestParam(name = "page", required = false, defaultValue = "0") int page,
														   @RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 5;
		}
		HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/system-functional", ConstantLog.SEARCH,
					principal.getName() + " search sf list");
			Page<SystemFunctional> systemFunctionals = systemFunctionalService.searchSystemFunctional(sfId, sfName, urlController, sfFather, sfStatus, realPage, size);
			for (SystemFunctional sf : systemFunctionals) {
				JSONObject js = sf.createJson();
				data.put(js, systemFunctionals.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("/admin/user/search")
	@PreAuthorize("hasAuthority('Quản lý tài khoản:Xem')")
	public Map<JSONObject, Integer> doSearchUser(@RequestParam String username, @RequestParam String fullname,
			@RequestParam String email, @RequestParam String phone, @RequestParam String groupId, @RequestParam String levelId,
			@RequestParam String areaId, @RequestParam String agencyId,@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 10;
		}
		HashMap<JSONObject, Integer> data = new LinkedHashMap<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/user-management", ConstantLog.SEARCH,
					principal.getName() + " search user");
			Page<Users> pages = usersService.searchUsers(username, fullname, email, phone, levelId, groupId, areaId, agencyId, realPage, size);
			for (Users users : pages) {
				JSONObject udata = users.createJson();
				data.put(udata, pages.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}

	@GetMapping("/admin/get/group-privileges/{groupId}")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Thực thi')")
	public Map<String, String> loadGroupPrivileges(@PathVariable String groupId, Principal principal) {
		HashMap<String, String> data = new HashMap<>();
		Set<String> setList = new HashSet<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.SEARCH,
					principal.getName() + " search privilege group user");
			List<GroupsLevelsFunctional> listGLF = glfService.findByGroupsId(groupId);
			for (GroupsLevelsFunctional glf : listGLF) {
				setList.add(glf.getSystemFunctionalGLF().getSfName() + ":" + glf.getPrivilegesGLF().getPrivilegeName());
			}
			for (String item : setList) {
				data.put(item, groupId);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return data;
	}

	@GetMapping("/admin/group/select")
//	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Xem')")
	public Map<JSONObject, Integer> selectGroup(@RequestParam long groupLevel,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "100") int size, Principal principal) {
		int realPage = page - 1;
		if (realPage - 1 < 0) {
			realPage = 0;
		}
		if (size < 0) {
			size = 100;
		}
		HashMap<JSONObject, Integer> data = new HashMap<>();
		try {
			insertLogService.insertLog(principal.getName(), "/vasonline/admin/group-management", ConstantLog.SEARCH,
					principal.getName() + " search group user");
			Groupss group = new Groupss();
			group.setLevelGroups(levelsService.findByLevelId(groupLevel));
			Page<Groupss> pages = gsService.selectGroup(group, realPage, size);
			for (Groupss groupss : pages) {
				JSONObject gdata = groupss.createJson();
				data.put(gdata, pages.getTotalPages());
			}
			return data;
		} catch (Exception e) {
			System.err.println(e);
			return data;
		}
	}
}
