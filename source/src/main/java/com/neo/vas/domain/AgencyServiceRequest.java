/**
 * 
 */
package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AgencyServiceRequest {
	@Id
	private long requestId;

	private long totalValue;
	private String description;
	private String customerAccount;
	private Integer status;
	private String message;
	private Long orderId;
	@CreatedBy
	private String createdBy;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdDate;
	@LastModifiedBy
	private String updatedBy;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date updatedDate;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agencyASR;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brandASR;
	
	public JSONObject createJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("requestId", requestId);
		json.put("totalValue", totalValue);
		json.put("description", description);
		json.put("customerAccount", customerAccount);
		json.put("status", status);
		json.put("message", message);
		json.put("orderId", orderId);
		json.put("createdBy", createdBy);
		json.put("createdDate", sdf.format(createdDate));
		json.put("updatedBy", updatedBy);
		json.put("updatedDate", updatedDate != null ? sdf.format(updatedDate) : "");
		json.put("agencyASR", agencyASR.getAgencyName());
		json.put("brandASR", brandASR.getBrandName());
		json.put("brandLevelId", brandASR.getApprovedBy());
		return json;
	}
}
