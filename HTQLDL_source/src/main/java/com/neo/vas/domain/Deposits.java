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
public class Deposits {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPOSITS_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_DEPOSITS", allocationSize = 1, name = "DEPOSITS_SEQ")
	private long id;

	private String depositsAmount;
	private int status;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date receivedDate;
	@CreatedBy
	private String createBy;
	@CreatedDate
	private Date createDate;
	private String depositNo;
	private String customerName;

	public String getAgencyCode() {
		return (agencyD == null) ? "" : agencyD.getAgencyCode();
	}

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agencyD;

	@ManyToOne
	@JoinColumn(name = "contract_id")
	private AgencyContract contractD;

	@OneToMany(mappedBy = "depositsID",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<ImageDeposits> imageDeposits = new HashSet<>();

	@Transient
	private String agencyCode;
	
	@Override
	public String toString() {
		return "Deposits [id=" + id + ", depositsAmount=" + depositsAmount + ", status=" + status + ", startDate="
				+ startDate + ", endDate=" + endDate +  ", createBy=" + createBy
				+ ", createDate=" + createDate + ", agencyD=" + agencyD + ", imageDeposits=" + imageDeposits + "]";
	}
	public JSONObject createJson(){
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject json = new JSONObject();
		json.put("id",id);
		json.put("depositsAmount",depositsAmount);
		json.put("status",status);
		json.put("startDate", (startDate == null) ? "" : fm.format(startDate));
		json.put("endDate", (endDate == null) ? "" : fm.format(endDate));
		json.put("createBy",createBy);
		json.put("createDate", (createDate == null) ? "" : fm.format(createDate));
		json.put("agencyName",agencyD.getAgencyName());
//		json.put("updateDate", (createDate == null) ? "" : fm.format(createDate));
		return json;
	}
}
