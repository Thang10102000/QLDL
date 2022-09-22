/**
 * 
 */
package com.neo.vas.domain;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
//import javax.annotation.Generated;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author KhanhBQ
 *
 */
//@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Users.class)
public abstract class Users_ {
	public static volatile SingularAttribute<Users, String> username;
	public static volatile SingularAttribute<Users, String> password;
	public static volatile SingularAttribute<Users, Integer> fullname;
	public static volatile SingularAttribute<Users, String> phone;
	public static volatile SingularAttribute<Users, String> email;
	public static volatile SingularAttribute<Users, String> address;
	public static volatile SingularAttribute<Users, String> fax;
	public static volatile SingularAttribute<Users, Integer> status;
	public static volatile SingularAttribute<Users, String> createBy;
	public static volatile SingularAttribute<Users, Date> createDate;
	public static volatile SingularAttribute<Users, String> lastModifyBy;
	public static volatile SingularAttribute<Users, Date> lastModifyDate;
	public static volatile SingularAttribute<Users, Date> lastLoginDate;
	public static volatile SingularAttribute<Users, Integer> passwordNeverExpired;
	public static volatile SingularAttribute<Users, Integer> authorized;
	public static volatile SetAttribute<Users, GroupsUsers> usersGroupsId;
	public static volatile SetAttribute<Users, UsersLevelsFunctional> usersULFId;
	public static volatile SetAttribute<Users, Levels> levelId;
	
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String FULLNAME = "fullname";
	public static final String PHONE = "phone";
	public static final String EMAIL = "email";
	public static final String ADDRESS = "address";
	public static final String FAX = "fax";
	public static final String STATUS = "status";
	public static final String CREATEBY = "createBy";
	public static final String CREATEDATE = "createDate";
	public static final String LASTMODIFYBY = "lastModifyBy";
	public static final String LASTMODIFYDATE = "lastModifyDate";
	public static final String LASTLOGINDATE = "lastLoginDate";
	public static final String PASSWORDNEVEREXPIRED = "passwordNeverExpired";
	public static final String AUTHORIZED = "authorized";
	public static final String USERSGROUPSID = "usersGroupsId";
	public static final String USERSULFID = "usersULFId";
	public static final String LEVELID = "levelId";
}
