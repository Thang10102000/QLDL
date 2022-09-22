//package com.neo.vas.service.specification;
//
//import org.springframework.data.jpa.domain.Specification;
//
//import com.neo.vas.domain.AMKAM;
//
///**
// * modifier: YNN
// */
//
//public final class AmkamSpecification {
//	public static Specification<AMKAM> hasAmkamCode(String amkamCode) {
//		return (root, query, cb) -> cb.like(cb.function("FN_CONVERT_TO_VN", String.class,
//				cb.upper(root.get("amkamCode").as(String.class))),
//				"%" + amkamCode.toUpperCase() + "%");
//	}
//
//	public static Specification<AMKAM> hasAmkamName(String amkamName) {
//		return (root, query, cb) -> cb.like(cb.function("FN_CONVERT_TO_VN", String.class,
//				cb.upper(root.get("amkamName").as(String.class))),
//				"%" + amkamName.toUpperCase() + "%");
//	}
//
//	public static Specification<AMKAM> hasIdCtkv(Object idCtkv) {
//		return (root, query, cb) -> cb.equal(root.get("agencyAreaId"), idCtkv);
//	}
//
//	public static Specification<AMKAM> hasNameCtkv(String nameCtkv) {
//		return (root, query, cb) -> cb.like(cb.function("FN_CONVERT_TO_VN", String.class,
//				cb.upper(root.get("nameCtkv").as(String.class))),
//				"%" + nameCtkv.toUpperCase() + "%");
//	}
//}
