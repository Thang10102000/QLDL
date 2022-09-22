package com.neo.vas.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.NamedNativeQuery;

@Entity
@NamedNativeQuery(name = "getReport", callable = true, query = "", resultClass = Report.class)
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	private String agencyName;

	public Report() {

	}

	public Report(String agencyName) {
		super();
		this.agencyName = agencyName;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

}
