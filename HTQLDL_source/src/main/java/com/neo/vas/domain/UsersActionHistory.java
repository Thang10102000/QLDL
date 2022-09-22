package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsersActionHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String description;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date createdTime;

	@ManyToOne
	@JoinColumn(name = "username")
	private Users usersUAH;

	@ManyToOne
	@JoinColumn(name = "sf_id")
	private SystemFunctional systemFunctionalUAH;

	@ManyToOne
	@JoinColumn(name = "privilege_id")
	private Privilegess privilegesUAH;
	
	public JSONObject createJson(){
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("id",id);
		json.put("username",usersUAH.getUsername());
		json.put("systemfunction",systemFunctionalUAH.getSfName());
		json.put("privileges",privilegesUAH.getPrivilegeName());
		json.put("description",description);
		json.put("createdDate",ft.format(createdTime));
		return json;
	}

}
