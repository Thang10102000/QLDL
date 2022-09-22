package com.neo.vas.service.specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.neo.vas.domain.SmsTemplate;
import com.neo.vas.domain.SmsTemplate_;

/**
 * @author KhanhBQ
 * 
 */
public final class SmsTemplateSpecification {
	public static Specification<SmsTemplate> hasSmsCode(String smsCode) {
		return (root, query, cb) -> cb.like(cb.lower(root.get(SmsTemplate_.TEMPCODE)).as(String.class),"%" + smsCode.toLowerCase() + "%");
	}

	public static Specification<SmsTemplate> hasStatus(Integer status) {
		return (root, query, cb) -> cb.equal(root.get(SmsTemplate_.STATUS), status);
	}

	public static Specification<SmsTemplate> hasStartDate(Date startDate) {
		return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(SmsTemplate_.STARTDATE), startDate);
	}

	public static Specification<SmsTemplate> hasEndDate(Date endDate) {
		return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(SmsTemplate_.ENDDATE), endDate);
	}
}
