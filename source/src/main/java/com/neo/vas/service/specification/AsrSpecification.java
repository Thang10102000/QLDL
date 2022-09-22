package com.neo.vas.service.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.AgencyServiceRequest;
import com.neo.vas.domain.AgencyServiceRequest_;

/**
 * modifier: YNN
 */

public final class AsrSpecification {
	public static Specification<AgencyServiceRequest> hasRequestId(Long requestId) {
		return (root, query, cb) -> cb.equal(root.get(AgencyServiceRequest_.REQUESTID), requestId);
	}

	public static Specification<AgencyServiceRequest> hasStatus(Integer status) {
		return (root, query, cb) -> cb.equal(root.get(AgencyServiceRequest_.STATUS), status);
	}

	public static Specification<AgencyServiceRequest> hasPackage(Object packageId) {
		return (root, query, cb) -> cb.equal(root.get("brandASR"), packageId);
	}

	public static Specification<AgencyServiceRequest> hasAgency(Object agencyId) {
		return (root, query, cb) -> cb.equal(root.get("agencyASR"), agencyId);
	}

	public static Specification<AgencyServiceRequest> hasCreatedDateFrom(Date created) {
		return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(AgencyServiceRequest_.CREATEDDATE).as(Date.class),
				created));
	}

	public static Specification<AgencyServiceRequest> hasCreatedDateTo(Date created) {
		return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(AgencyServiceRequest_.CREATEDDATE).as(Date.class),
				created));
	}

	public static Specification<AgencyServiceRequest> hasUpdatedDateFrom(Date modified) {
		return ((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(AgencyServiceRequest_.UPDATEDDATE).as(Date.class),
				modified));
	}

	public static Specification<AgencyServiceRequest> hasUpdatedDateTo(Date modified) {
		return ((root, query, cb) -> cb.lessThanOrEqualTo(root.get(AgencyServiceRequest_.UPDATEDDATE).as(Date.class),
				modified));
	}
}
