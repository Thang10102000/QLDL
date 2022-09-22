/**
 * 
 */
package com.neo.vas.domain;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * @author KhanhBQ
 *
 */
@StaticMetamodel(SmsTemplate.class)
public abstract class SmsTemplate_ {
	public static volatile SingularAttribute<SmsTemplate, String> tempCode;
	public static volatile SingularAttribute<SmsTemplate, String> content;
	public static volatile SingularAttribute<SmsTemplate, String> description;
	public static volatile SingularAttribute<SmsTemplate, Date> startDate;
	public static volatile SingularAttribute<SmsTemplate, Date> endDate;
	public static volatile SingularAttribute<SmsTemplate, Integer> status;
	public static volatile SingularAttribute<SmsTemplate, String> updatedBy;
	public static volatile SingularAttribute<SmsTemplate, Date> updatedTime;

	public static final String TEMPCODE = "tempCode";
	public static final String CONTENT = "content";
	public static final String DESCRIPTION = "description";
	public static final String STARTDATE = "startDate";
	public static final String ENDDATE = "endDate";
	public static final String STATUS = "status";
	public static final String UPDATEDBY = "updatedBy";
	public static final String UPDATEDTIME = "updatedTime";
}
