/**
 * 
 */
package com.neo.vas.service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.Users;
import com.neo.vas.domain.Users_;

/**
 * @author KhanhBQ
 *
 */
public final class UsersSpecification {

	public static Specification<Users> hasUsername(String username) {
		return (root, query, cb) -> cb.like(
				cb.function("FN_CONVERT_TO_VN", String.class, cb.upper(root.get(Users_.USERNAME).as(String.class))),
				"%" + username.toUpperCase() + "%");
	}

	public static Specification<Users> equalUsername(String username) {
		return (root, query, cb) -> cb.equal(cb.lower(root.get(Users_.USERNAME).as(String.class)),
				username.toLowerCase());
	}

	public static Specification<Users> hasFullname(String fullname) {
		return (root, query, cb) -> cb.like(cb.lower(root.get(Users_.FULLNAME).as(String.class)),
				"%" + fullname.toLowerCase() + "%");
	}

	public static Specification<Users> hasPhone(String phone) {
		return (root, query, cb) -> cb.like(root.get(Users_.PHONE).as(String.class), "%" + phone + "%");
	}

	public static Specification<Users> hasEmail(String email) {
		return (root, query, cb) -> cb.like(cb.lower(root.get(Users_.EMAIL).as(String.class)),
				"%" + email.toLowerCase() + "%");
	}

	public static Specification<Users> hasAddress(String address) {
		return (root, query, cb) -> cb.like(cb.lower(root.get(Users_.ADDRESS).as(String.class)),
				"%" + address.toLowerCase() + "%");
	}

	public static Specification<Users> hasFax(String fax) {
		return (root, query, cb) -> cb.like(root.get(Users_.FAX).as(String.class), "%" + fax + "%");
	}

	public static Specification<Users> hasStatus(String status) {
		return (root, query, cb) -> cb.like(root.get(Users_.STATUS), status);
	}

	public static Specification<Users> hasCreateBy(String createBy) {
		return (root, query, cb) -> cb.like(root.get(Users_.CREATEBY), createBy);
	}

	public static Specification<Users> hasCreateDate(String createDate) {
		return (root, query, cb) -> cb.like(root.get(Users_.CREATEDATE), createDate);
	}

	public static Specification<Users> hasLastModifyBy(String lastModifyBy) {
		return (root, query, cb) -> cb.like(root.get(Users_.LASTMODIFYBY), lastModifyBy);
	}

	public static Specification<Users> hasLastModifyDate(String lastModifyDate) {
		return (root, query, cb) -> cb.like(root.get(Users_.LASTMODIFYDATE), lastModifyDate);
	}

	public static Specification<Users> hasPasswordNeverExpired(String passwordNeverExpired) {
		return (root, query, cb) -> cb.like(root.get(Users_.PASSWORDNEVEREXPIRED), passwordNeverExpired);
	}

	public static Specification<Users> hasAuthorized(String authorized) {
		return (root, query, cb) -> cb.like(root.get(Users_.AUTHORIZED), authorized);
	}

	public static Specification<Users> hasUsersGroups(String usersGroupsId) {
		return (root, query, cb) -> cb.like(root.get(Users_.USERSGROUPSID), usersGroupsId);
	}

	public static Specification<Users> hasUsersULF(String usersULFId) {
		return (root, query, cb) -> cb.like(root.get(Users_.USERSULFID), usersULFId);
	}

	public static Specification<Users> hasLevelUsers(String levelId) {
		return (root, query, cb) -> cb.like(root.get(Users_.LEVELID), levelId);
	}
}
