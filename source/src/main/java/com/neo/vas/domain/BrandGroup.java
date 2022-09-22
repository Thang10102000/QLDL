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
public class BrandGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BG_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_BRAND_GROUP", allocationSize = 1, name = "BG_SEQ")
	private long id;

	private String groupName;
	private int status;
	private String description;
	private Date updateDate;
	private String updateBy;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "service_id")
	private Services servicesBG;

	@OneToMany(mappedBy = "brandGroupB", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Brand> brands = new HashSet<>();

	public JSONObject createJson(){
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("groupName",groupName);
		json.put("status",status);
		json.put("description",(description == null) ? "" : description);
		json.put("serviceBG",servicesBG.getServiceName());
		json.put("createdDate", (createdDate == null) ? "" : fm.format(createdDate));
		json.put("updateDate", (updateDate == null) ? "" : fm.format(updateDate));
		return json;
	}
}
