/**
 * 
 */
package com.neo.vas.service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.GroupsUsers;
import com.neo.vas.domain.GroupsUsers_;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.Users;

/**
 * @author KhanhBQ
 *
 */
public final class GroupUserSpecification {
	public static Specification<GroupsUsers> equalGroupId(String id) {
		return (root, query, cb) -> cb.equal(root.get(GroupsUsers_.GROUP_USER_ID).as(String.class), id);
	}

	public static Specification<GroupsUsers> hasUser(Users username) {
		return (root, query, cb) -> cb.equal(root.get(GroupsUsers_.GROUP_USER_USERS), username);
	}

	public static Specification<GroupsUsers> hasGroup(Groupss group) {
		return (root, query, cb) -> cb.equal(root.get(GroupsUsers_.GROUP_USER_GROUP_ID), group);
	}
}
