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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

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
public class AgencyOrders {
	@Id
	private long orderId;

	private long orderValue;
	private long orderPay;
	private String description;
	private long paymentMethod;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;

	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	@LastModifiedBy
	private String updatedBy;
	@LastModifiedDate
	private Date updatedDate;
	private Long currentValue;
	private Long consumptionAmount;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agencyAO;

	@ManyToOne
	@JoinColumn(name = "status_id")
	private OrderStatus orderStatusAO;
	
	@OneToMany(mappedBy = "agencyOrdersAOP", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyOrderPayment> agencyOrderPayment = new HashSet<>();

	@OneToMany(mappedBy = "agencyOrders", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyOrderRequest> agencyOrderRequests = new HashSet<>();

	@Override
	public String toString() {
		return "AgencyOrders [orderId=" + orderId + ", orderValue=" + orderValue + ", orderPay=" + orderPay
				+ ", description=" + description + ", paymentMethod=" + paymentMethod + " ,startDate=" + startDate
				+ ", endDate=" + endDate + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy="
				+ updatedBy + ", updatedDate=" + updatedDate + "]";
	}

	public JSONObject createJson(){
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("orderId",orderId);
		json.put("orderValue",orderValue);
		json.put("orderPay",orderPay);
		json.put("description",(description == null) ? "" : description);
		json.put("paymentMethod",paymentMethod);
		json.put("currentValue", (currentValue == null) ? 0 : currentValue);
		json.put("startDate",ft.format(startDate));
		json.put("endDate",ft.format(endDate));
		json.put("agencyAO",agencyAO.getAgencyName());
		json.put("orderStatusAO",orderStatusAO.getId());
		json.put("orderStatusAOName",orderStatusAO.getStatusName());
		json.put("consumptionAmount",(consumptionAmount == null) ? 0 : consumptionAmount);
		json.put("createdDate", (createdDate==null) ? "" : ft.format(createdDate));
		json.put("updateDate", (updatedDate==null) ? "" : ft.format(updatedDate));
		json.put("agencyArea", agencyAO.getAreaId().getAreaName());
		return json;
	}
}
