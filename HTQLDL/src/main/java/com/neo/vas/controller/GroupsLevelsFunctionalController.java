/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.util.ArrayList;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.service.GroupsLevelsFunctionalService;
import com.neo.vas.service.InsertLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class GroupsLevelsFunctionalController {
	@Autowired
	private GroupsLevelsFunctionalService glfService;
	@Autowired
	private InsertLogService insertLogService;
	
	@PostMapping("/admin/group/save/privileges")
	@PreAuthorize("hasAnyAuthority('Quản lý nhóm tài khoản:Sửa')")
	public ResponseEntity<String> savePrivileges(@RequestBody ArrayList<String> list, Principal principal){
		String groupId = list.get(list.size() - 1);
		list.remove(list.size() - 1);
		try {
			glfService.savePrivilegesByGroupId(list, groupId);
			insertLogService.insertLog(principal.getName(),"/vasonline/admin/group-management", ConstantLog.EDIT,
					principal.getName()+" edit group management "+groupId+" success");
			return ResponseEntity.status(HttpStatus.OK).body("Cập nhật quyền thành công cho nhóm tài khoản: " + groupId);
		} catch (Exception e) {
			System.err.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Cập nhật cho nhóm tài khoản: " + groupId + " thất bại");
		}

	}
}
