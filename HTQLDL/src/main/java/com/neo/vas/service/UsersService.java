/**
 * 
 */
package com.neo.vas.service;

import java.security.Principal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.Users;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
public interface UsersService {
	public Users getUsersByUsername(String username);

	public Page<Users> searchUsers(String username, String fullname, String email, String phone, String groupId,
			String levelId, String areaId, String agencyId, int page, int pageSize);

	public String saveNewUser(JSONObject data, Principal principal);

	public String editUser(JSONObject data, Principal principal);

	public boolean deleteUser(String username, Principal principal);

	public boolean checkExistById(String username);

	public Page<Users> loadUsers(String username, int page, int pageSize);

	public List<Users> getAll();

	public String changePassword(JSONObject data, Principal principal);
}
