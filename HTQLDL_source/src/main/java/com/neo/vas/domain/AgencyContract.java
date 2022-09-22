/**
 * 
 */
package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author KhanhBQ
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgencyContract {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AC_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_AGENCY_CONTRACT", allocationSize = 1, name = "AC_SEQ")
	private long id;

	private String contractNo;
	private String serviceType;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date signDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	private long guaranteeValue;
	private int status;
	private int liquidationDate;
	@CreatedBy
	private String createBy;
	private String updateBy;
	@CreatedDate
	private Date createDate;
	private Date updateDate;
	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agencyAC;
	
	@OneToMany(mappedBy = "agencyContractID",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<ImageDeposits> imageDeposits = new HashSet<>();

	@OneToMany(mappedBy = "contractD")
	@JsonIgnore
	private Set<Deposits> deposits = new HashSet<>();

	@Transient
	private String agencyCode;

	@Override
	public String toString() {
		return "AgencyContract [id=" + id + ", contractNo=" + contractNo + ", serviceType=" + serviceType
				+ ", signDate=" + signDate + ", startDate=" + startDate + ", endDate=" + endDate + ", guaranteeValue="
				+ guaranteeValue + ", status=" + status + ", liquidationDate=" + liquidationDate + ", createBy="
				+ createBy + ", createDate=" + createDate + ", agencyAC=" + agencyAC.getAgencyName() + "]";
	}

	public JSONObject createJson() {
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("contractNo", contractNo);
		json.put("serviceType", serviceType);
		json.put("signDate", (signDate==null) ? "" : ft.format(signDate));
		json.put("startDate", (startDate==null) ? "" : ft.format(startDate));
		json.put("endDate", (endDate==null) ? "" : ft.format(endDate));
		json.put("guaranteeValue", guaranteeValue);
		json.put("status", status);
		json.put("agencyName", agencyAC.getAgencyName());
		json.put("areaName",agencyAC.getAreaId().getAreaName());
		json.put("createdDate", (createDate==null) ? "" : ft.format(createDate));
		json.put("updateDate", (updateDate==null) ? "" : ft.format(updateDate));
		return json;
	}

	public long getId() {
		return id;
	}

	public String getContractNo() {
		return contractNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public Date getSignDate() {
		return signDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public long getGuaranteeValue() {
		return guaranteeValue;
	}

	public int getStatus() {
		return status;
	}

	public int getLiquidationDate() {
		return liquidationDate;
	}

	public String getCreateBy() {
		return createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Agency getAgencyAC() {
		return agencyAC;
	}

	public String getAgencyCode() {
		return (agencyAC != null) ? agencyAC.getAgencyCode() : "";
	}
}
