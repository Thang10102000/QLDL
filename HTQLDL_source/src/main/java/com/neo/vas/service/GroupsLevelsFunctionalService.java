/**
 * 
 */
package com.neo.vas.service;

import com.neo.vas.domain.GroupsLevelsFunctional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KhanhBQ
 *
 */
public interface GroupsLevelsFunctionalService {
	boolean savePrivilegesByGroupId(ArrayList<String> privileges, String groupId);
	List<GroupsLevelsFunctional> findByUsersname(String username);
	List<GroupsLevelsFunctional> findByGroupsId(String groupId);
}
