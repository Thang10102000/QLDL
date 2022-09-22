/**
 * 
 */
package com.neo.vas.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;

import com.neo.vas.constant.ConstantLog;
import com.neo.vas.service.InsertLogService;
import com.neo.vas.service.UserLevelFunctionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.neo.vas.service.impl.UserLevelFunctionalServiceImpl;

/**
 * @author KhanhBQ
 *
 */
@Controller
public class UserLevelFunctionalController {
	@Autowired
	private UserLevelFunctionalService ulfService;
	@Autowired
	private InsertLogService insertLogService;

	@PostMapping("/admin/save/privileges")
	@PreAuthorize(value = "hasAnyAuthority('Quản lý tài khoản:Sửa')")
	public ResponseEntity<String> savePrivileges(@RequestBody ArrayList<String> list, Principal principal) throws SQLException {
		String username = list.get(list.size() - 1);
		list.remove(list.size() - 1);
		try {
			if(ulfService.savePrivilegesByUsername(list, username)) {
				insertLogService.insertLog(principal.getName(),"/vasonline/admin/user-management", ConstantLog.EDIT,
						principal.getName()+" update privilege user success");
				return ResponseEntity.status(HttpStatus.OK).body("Cập nhật quyền thành công cho tài khoản: " + username);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Cập nhật quyền cho tài khoản: " + username + " thất bại");
		} catch (Exception e) {
			System.err.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Cập nhật quyền cho tài khoản: " + username + " thất bại");
		}
	}
}
