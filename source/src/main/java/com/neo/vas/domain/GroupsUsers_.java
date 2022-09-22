/**
 * 
 */
package com.neo.vas.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author KhanhBQ
 *
 */
@StaticMetamodel(GroupsUsers.class)
public abstract class GroupsUsers_ {
	public static volatile SingularAttribute<GroupsUsers, String> guId;
	public static volatile SingularAttribute<GroupsUsers, Users> usersGU;
	public static volatile SingularAttribute<GroupsUsers, Groupss> groupIdGU;

	public static final String GROUP_USER_ID = "guId";
	public static final String GROUP_USER_USERS = "usersGU";
	public static final String GROUP_USER_GROUP_ID = "groupIdGU";
}
