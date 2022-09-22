/**
 * 
 */
package com.neo.vas.service;

import com.neo.vas.domain.UsersLevelsFunctional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KhanhBQ
 *
 */
public interface UserLevelFunctionalService {
	boolean savePrivilegesByUsername(ArrayList<String> privileges, String username);
	List<UsersLevelsFunctional> findByUsersname(String username);
	//void deleteULF(String username, long sfId);
	void deleteULF(long ulfId);
}
