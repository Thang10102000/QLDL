/**
 * 
 */
package com.neo.vas.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * @author KhanhBQ
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgencyDiscountCommission {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADC_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_AGENCY_DISCOUNT_COMMISSION", allocationSize = 1, name = "ADC_SEQ")
	private long id;
//	0: chính sách hoa hồng, 1: chính sách chiết khấu
	private int type;

	@ManyToOne
	@JoinColumn(name = "pod_id")
	private DiscountCommission discountPolicyAD;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agencyAD;


	public JSONObject createJson(){
		JSONObject json = new JSONObject();
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		json.put("id",id);
		json.put("agencyName", agencyAD.getAgencyName());
		json.put("policyName",discountPolicyAD.getPolicyName());
		json.put("areaName",agencyAD.getAreaId().getAreaName());
		json.put("startDate",(discountPolicyAD.getStartDate() == null) ? "" : ft.format(discountPolicyAD.getStartDate()));
		json.put("endDate",(discountPolicyAD.getEndDate() == null) ? "" : ft.format(discountPolicyAD.getEndDate()));
		return json;
	}
}
