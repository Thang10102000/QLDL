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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * project_name: vasonline Created_by: thulv time: 11/05/2021
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizedPerson {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AP_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_AUTHORIZED_PERSON", allocationSize = 1, name = "AP_SEQ")
	private long id;
	private String fullName;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date birthday;

	private String position;
	private int type;
	private String phoneNumber;
	private String telephone;
	private String email;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "agency_id")
	private Agency agencyIdPerson;

	@Override
	public String toString() {
		return "AuthorizedPerson [id=" + id + ", fullName=" + fullName + ", birthday=" + birthday + ", position="
				+ position + ", phoneNumber=" + phoneNumber + ", email=" + email + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", agencyIdPerson=" + agencyIdPerson + "]";
	}

	public JSONObject createJson(){
		SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
		JSONObject json = new JSONObject();
		json.put("id", id);
		json.put("fullName",fullName);
		json.put("birthday", (birthday == null) ? "" : fm.format(birthday));
		json.put("position",(position == null) ? "" : position);
		json.put("phoneNumber",(phoneNumber== null) ? "" : phoneNumber);
		json.put("email",(email == null) ? "" : email);
		json.put("type",type);
		json.put("agencyIdPerson",agencyIdPerson.getAgencyName());
		return json;
	}
}
