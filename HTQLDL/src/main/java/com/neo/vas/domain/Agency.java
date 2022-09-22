package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * project_name: vasonline Created_by: thulv time: 11/05/2021
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Agency {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AG_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_AGENCY", allocationSize = 1, name = "AG_SEQ")
	private long id;

	private String agencyName;
	private int agencyType;
	private String businessLicense;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date licenseDate;

	private String licensePlace;
	private String officeAddress;
	private String invoiceAddress;
	private String taxCode;
	private String phoneNumber;
	private String email;
	private String business;
	private String vasNumber;
	private int status;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	private Date updateDate;
	private String updateBy;
	private String agencyCode;
	private String branchAgency;
	private int type;
	private Long surplus;
	private String guaranteeAmount;
	private String shopCode;

	@ManyToOne
	@JoinColumn(name = "area_id")
	private AgencyArea areaId;

	@OneToMany(mappedBy = "agencyId", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyBankAccount> agencyBankAccountSet = new HashSet<>();

	@OneToMany(mappedBy = "agencyIdPerson", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AuthorizedPerson> authorizedPeople = new HashSet<>();

	@OneToMany(mappedBy = "agencyAD", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyDiscountCommission> agencyDiscount = new HashSet<>();

	@OneToMany(mappedBy = "agencyD", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Deposits> deposits = new HashSet<>();

	@OneToMany(mappedBy = "agencyAC", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyContract> agencyContract = new HashSet<>();

	@OneToMany(mappedBy = "agencyASR", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyServiceRequest> agencyServiceRequest = new HashSet<>();

	@OneToMany(mappedBy = "agencyAO", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyOrders> agencyOrders = new HashSet<>();

	public JSONObject createJson() {
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("agencyCode", agencyCode);
		json.put("agencyName", agencyName);
		json.put("branchAgency", branchAgency);
		json.put("areaIdId", areaId.getAreaId());
		json.put("areaId", areaId.getAreaName());
		json.put("agencyType", agencyType);
		json.put("phoneNumber", phoneNumber);
		json.put("vasNumber", (vasNumber == null) ? "" : vasNumber);
		json.put("shopCode", (shopCode == null) ? "" : shopCode);
		json.put("status", status);
		json.put("email",  email);
		json.put("createdDate", (createdDate == null) ? "" : fm.format(createdDate));
		json.put("updateDate", (updateDate == null) ? "" : fm.format(updateDate));
		json.put("surplus", (surplus==null) ? 0 : surplus);
		return json;
	}

	public JSONObject createJsonSearch(){
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("agencyName", agencyName);
		return json;
	}
}
