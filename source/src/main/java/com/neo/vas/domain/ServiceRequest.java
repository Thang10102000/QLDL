package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SR_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_SERVICE_REQUEST_V2", allocationSize = 1, name = "SR_SEQ")
	private long srId;
	private long status;
	private String description;
	private String policy;
	private long price;
	private long discountCost;
	private long quantity;
	private Integer amount;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date approved;
	@CreatedBy
	private String creator;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date created;
	@LastModifiedBy
	private String modifier;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date modified;
	private Integer remainingQuantity;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brandASR;
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "serviceRequest", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ServiceRequestActivate> srActive = new HashSet<>();

	@OneToMany(mappedBy = "serviceRequest", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyOrderRequest> orderRequests = new HashSet<>();

	public JSONObject createJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("srId", srId);
		json.put("description", description);
		json.put("policy", policy);
		json.put("status", status);
		json.put("price", price);
		json.put("discountCost", discountCost);
		json.put("quantity", quantity);
		json.put("amount", amount);
		json.put("creator", creator);
		json.put("created", sdf.format(created));
		json.put("modifier", modifier);
		json.put("modified", modified != null ? sdf.format(modified) : "");
		json.put("approved", approved != null ? sdf.format(approved) : "");
		json.put("brandName", brandASR.getBrandName());
		json.put("brandLevelId", brandASR.getApprovedBy());
		json.put("customerName", customer.getName());
		json.put("agencyArea", customer.getAreaCId().getAreaName());
		json.put("remainingQuantity", remainingQuantity);
		return json;
	}
}
