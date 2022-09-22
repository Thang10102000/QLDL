package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiTransactionLog {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATL_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_API_TRANSACTION_LOG", allocationSize = 1, name = "ATL_SEQ")
	private long logId;
	private String url;
	private String request;
	private String response;
	private Long orderId;
	@CreatedBy
	private String creator;
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	private Date created;
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
	private Date responseTime;

	public JSONObject createJson(){
		JSONObject json = new JSONObject();
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		json.put("logId", logId);
		json.put("created", ft.format(created));
		json.put("creator", creator);
		json.put("response", (response == null) ? "" : response);
		json.put("request",request);
		json.put("responseTime", responseTime == null ? "" : ft.format(responseTime));
		json.put("url",url);
		return json;
	}
}
