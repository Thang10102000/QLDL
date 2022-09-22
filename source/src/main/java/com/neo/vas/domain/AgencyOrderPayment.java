/**
 * 
 */
package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
public class AgencyOrderPayment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AOP_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_AGENCY_ORDER_PAYMENT", allocationSize = 1, name = "AOP_SEQ")
	private long id;
	
	private String paymentMethod;
	private long amount;
	private String receiverName;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date receiverTime;
	private String description;
	@CreatedDate
	private Date createdDate;
	@CreatedBy
	private String createdBy;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private AgencyOrders agencyOrdersAOP;

	public JSONObject createJson(){
		JSONObject json= new JSONObject();
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
		json.put("id",id);
		json.put("paymentMethod",paymentMethod);
		json.put("amount",amount);
		json.put("receiverName",receiverName);
		json.put("orderId",agencyOrdersAOP.getOrderId());
		json.put("receiverTime",ft.format(receiverTime));
		json.put("description",(description == null ) ? "" : description);
		return json;
	}
}
