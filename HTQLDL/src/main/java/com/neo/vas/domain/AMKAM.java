/**
 * 
 */
package com.neo.vas.domain;

import lombok.*;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author YNN
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AMKAM {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private String amkamCode;
	private String amkamName;
	@Column(name = "ID_CTKV")
	private Long areaId;
	@Column(name ="NAME_CTKV")
	private String areaName;

	@CreatedDate
	@Column(name = "CREATED")
	private Date createDate;
	
	private static AMKAM instance;

	public static synchronized AMKAM getInstance() throws Exception
	{
		if(instance == null)
		{
			instance = new AMKAM();
		}
		return instance;
	}

	public JSONObject createJson() {
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("amkamCode", amkamCode);
		jsonObject.put("amkamName", amkamName);
		jsonObject.put("areaId", areaId);
		jsonObject.put("areaName", areaName);
		jsonObject.put("createdDate", (createDate == null) ? "" : fm.format(createDate));
		return jsonObject;
	}

}
