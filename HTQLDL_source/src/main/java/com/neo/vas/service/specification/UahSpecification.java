package com.neo.vas.service.specification;

import java.util.Date;

import com.neo.vas.domain.Privilegess;
import com.neo.vas.domain.SystemFunctional;
import com.neo.vas.domain.Users;
import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.UsersActionHistory;

public class UahSpecification {
	public static Specification<UsersActionHistory> hasUsername(Users username) {
		return (root, query, cb) -> cb.equal(root.get("usersUAH"), username);
	}

	public static Specification<UsersActionHistory> hasModule(SystemFunctional module) {
		return (root, query, cb) -> cb.equal(root.get("systemFunctionalUAH"), module);
	}

	public static Specification<UsersActionHistory> hasPrivileges(Privilegess privileges) {
		return (root, query, cb) -> cb.equal(root.get("privilegesUAH"), privileges);
	}

	public static Specification<UsersActionHistory> hasCreatedDateFrom(Date created) {
		return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdTime").as(Date.class), created));
	}

	public static Specification<UsersActionHistory> hasCreatedDateTo(Date created) {
		return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdTime").as(Date.class), created));
	}

}
