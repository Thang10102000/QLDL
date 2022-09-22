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
@StaticMetamodel(Groupss.class)
public abstract class Groups_ {
	public static volatile SingularAttribute<Groupss, String> groupId;
	public static volatile SingularAttribute<Groupss, String> groupName;
	public static volatile SingularAttribute<Groupss, Groupss> groupFather;
	public static volatile SingularAttribute<Levels, Groupss> levelGroups;

	public static final String GROUP_ID = "groupId";
	public static final String GROUP_NAME = "groupName";
	public static final String GROUP_FATHER = "groupFather";
	public static final String GROUP_LEVEL = "levelGroups";
}
