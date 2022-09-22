/**
 * 
 */
package com.neo.vas.service;

import com.neo.vas.domain.Groupss;
import org.springframework.data.domain.Page;

import com.neo.vas.domain.GroupsUsers;

/**
 * @author KhanhBQ
 *
 */
public interface GroupUserService {
	public Page<GroupsUsers> searchGroupUsers(GroupsUsers user, int page, int pageSize);
	public boolean deleteUserFromGroup(GroupsUsers gu);
	GroupsUsers findByGuId(Long id);
}
