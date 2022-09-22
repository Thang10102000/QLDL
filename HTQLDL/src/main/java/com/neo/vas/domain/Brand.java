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

/**
 * @author KhanhBQ
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BRAND_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_BRAND", allocationSize = 1, name = "BRAND_SEQ")
	private long id;

	private String brandId;
	private String brandName;
	private long price;
	private long discountRate;
	private long priceDiscount;
	private Long dcPolicyId;
	private int commissionRate;
	private int activeDay;
	private int status;
	private int brandType;
	private long approvedBy;
	private String description;
	private Date updateDate;
	private String updateBy;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private BrandGroup brandGroupB;

	@OneToMany(mappedBy = "brandBP", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<BrandCommissionPolicy> brandCommissionP = new HashSet<>();

	@OneToMany(mappedBy = "brandASR", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyServiceRequest> agencyServiceRequest = new HashSet<>();

	public JSONObject createJson(){
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id",id);
		jsonObject.put("brandId",brandId);
		jsonObject.put("brandName",brandName);
		jsonObject.put("price",price);
		jsonObject.put("discountRate",discountRate);
		jsonObject.put("commissionRate",commissionRate);
		jsonObject.put("priceDiscount",priceDiscount);
		jsonObject.put("activeDay",activeDay);
		jsonObject.put("status",status);
		jsonObject.put("brandType",brandType);
		jsonObject.put("description",(description == null) ? "" : description);
		jsonObject.put("level",approvedBy);
		jsonObject.put("dcPolicyId",(dcPolicyId == null) ? commissionRate : dcPolicyId);
		jsonObject.put("brandGroupB",(brandGroupB == null) ? "" : brandGroupB.getGroupName());
		jsonObject.put("createdDate", (createdDate == null) ? "" : fm.format(createdDate));
		jsonObject.put("updateDate", (updateDate == null) ? "" : fm.format(updateDate));
		return jsonObject;
	}
}
