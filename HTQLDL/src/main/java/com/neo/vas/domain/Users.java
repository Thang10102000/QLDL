/**
 * 
 */
package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

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

/**
 * @author KhanhBQ
 * @modifier YNN
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
	@Id
	private String username;

	private String password;
	private String fullname;
	private String phone;
	@Email
	private String email;
	private String address;
	private String fax;
	private int status;
	@CreatedBy
	private String createBy;
	@CreatedDate
	private Date createDate;
	@LastModifiedBy
	private String lastModifyBy;
	@LastModifiedDate
	private Date lastModifyDate;
	private Date lastLoginDate;
	private int passwordNeverExpired;
	private int authorized;

	@OneToMany(mappedBy = "usersGU", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<GroupsUsers> groupsUsers = new HashSet<>();

	@OneToMany(mappedBy = "usersULF", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<UsersLevelsFunctional> usersULF = new HashSet<>();

	@OneToMany(mappedBy = "usersUAH", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<UsersActionHistory> usersUAH = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "level_id")
	private Levels levelUsers;
	
	@ManyToOne
	@JoinColumn(name = "area_id")
	private AgencyArea areaId;
	
	private Long agencyId;

	@Override
	public String toString() {
		return "Users [username=" + username + ", password=" + password + ", fullname=" + fullname + ", phone=" + phone
				+ ", email=" + email + ", address=" + address + ", fax=" + fax + ", status=" + status + ", createBy="
				+ createBy + ", createDate=" + createDate + ", lastModifyBy=" + lastModifyBy + ", lastModifyDate="
				+ lastModifyDate + ", lastLoginDate=" + lastLoginDate + ", passwordNeverExpired=" + passwordNeverExpired
				+ ", authorized=" + authorized + "]";
	}

	public JSONObject createJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("username", (username == null) ? "" : username);
		json.put("fullname", (fullname == null) ? "" : fullname);
		json.put("phone", (phone == null) ? "" : phone);
		json.put("email", (email == null) ? "" : email);
		json.put("address", (address == null) ? "" : address);
		json.put("fax", (fax == null) ? "" : fax);
		json.put("status", status);
		json.put("createBy", (createBy == null) ? "" : createBy);
		json.put("createDate", (createDate == null) ? "" : sdf.format(createDate));
		json.put("lastLoginDate", (lastLoginDate == null) ? "" : sdf2.format(lastLoginDate));
		json.put("levelUsers", (levelUsers == null) ? "" : levelUsers.getLevelName());
		json.put("areaId", (areaId == null) ? "" : areaId.getAreaName());
		json.put("agencyId", (agencyId == null) ? "" : agencyId);
		return json;
	}
}
