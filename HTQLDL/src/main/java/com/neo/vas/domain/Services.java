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
public class Services {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SV_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_SERVICE", allocationSize = 1, name = "SV_SEQ")
	private long id;

	private String serviceId;
	private String serviceName;
	private String shortCode;
	private int status;
	private String description;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	private Date updateDate;
	private String updateBy;
	@OneToMany(mappedBy = "servicesBG", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<BrandGroup> brandGroups = new HashSet<>();

	public JSONObject createJson(){
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("id",id);
		json.put("serviceId",serviceId);
		json.put("serviceName",serviceName);
		json.put("shortCode",shortCode);
		json.put("status",status);
		json.put("description", (description == null) ? "" : description);
		json.put("createdDate", (createdDate == null) ? "" : fm.format(createdDate));
		json.put("updateDate", (updateDate == null) ? "" : fm.format(updateDate));
		return json;
	}
}
