/**
 * 
 */
package com.neo.vas.service.specification;

import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.Groups_;
import com.neo.vas.domain.Groupss;
import com.neo.vas.domain.Levels;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
public final class GroupSpecification {
	
	public static Specification<Groupss> hasGroupId(String id) {
		return (root, query, cb) -> cb.like(
				cb.function("FN_CONVERT_TO_VN", String.class, cb.upper(root.get(Groups_.GROUP_ID).as(String.class))),
				"%" + id.toUpperCase() + "%");
	}

	public static Specification<Groupss> equalGroupId(String id) {
		return (root, query, cb) -> cb.equal(root.get(Groups_.GROUP_ID).as(String.class), id);
	}

	public static Specification<Groupss> hasGroupName(String name) {
		return (root, query, cb) -> cb.like(
				cb.function("FN_CONVERT_TO_VN", String.class, cb.upper(root.get(Groups_.GROUP_NAME).as(String.class))),
				"%" + name.toUpperCase() + "%");
	}

	public static Specification<Groupss> hasGroupFather(Groupss father) {
		return (root, query, cb) -> cb.equal(root.get(Groups_.GROUP_FATHER), father);
	}

	public static Specification<Groupss> equalGroupLevel(Levels level) {
		return (root, query, cb) -> cb.equal(root.get(Groups_.GROUP_LEVEL), level);
	}
}
