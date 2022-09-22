/**
 * 
 */
package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.json.JSONObject;

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
public class SmsTemplate {
	@Id
	private String tempCode;
	private String content;
	private String description;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date endDate;
	private int status;
	private String updatedBy;
	private Date updatedTime;

	public JSONObject createJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject json = new JSONObject();
		json.put("tempCode", tempCode);
		json.put("content", content);
		json.put("description", description);
		json.put("startDate", (startDate == null ) ? "" : sdf.format(startDate));
		json.put("endDate",(endDate == null ) ? "" : sdf.format(endDate));
		json.put("status", status);
		json.put("updatedBy", updatedBy);
		json.put("updatedTime",(updatedTime == null) ? "" : sdf.format(updatedTime));
		return json;
	}
}
