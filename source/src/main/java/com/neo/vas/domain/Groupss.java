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
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Groupss {
	@Id
	private String groupId;

	private String groupName;

	@ManyToOne
	@JoinColumn(name = "group_father_id")
	private Groupss groupFather;

	@OneToMany(mappedBy = "groupFather", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Groupss> childGroup = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "level_id")
	private Levels levelGroups;

	@OneToMany(mappedBy = "groupsGLF", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<GroupsLevelsFunctional> groupsGLF = new HashSet<>();

	@CreatedBy
	private String createBy;
	@CreatedDate
	private Date createDate;
	@LastModifiedBy
	private String lastModifyBy;
	@LastModifiedDate
	private Date lastModifyDate;

	@OneToMany(mappedBy = "groupIdGU", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<GroupsUsers> groupsGroups = new HashSet<>();

	@Override
	public String toString() {
		return "Groupss [groupId=" + groupId + ", groupName=" + groupName + ", childGroup=" + childGroup
				+ ", levelGroups=" + levelGroups + ", groupsGLF=" + groupsGLF + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", lastModifyBy=" + lastModifyBy + ", lastModifyDate=" + lastModifyDate
				+ "]";
	}

	public JSONObject createJson() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		JSONObject json = new JSONObject();
		json.put("groupId", groupId);
		json.put("groupName", groupName);
		if (null != groupFather) {
			json.put("groupFather", groupFather.groupName);
		} else {
			json.put("groupFather", "");
		}
		if (null != levelGroups) {
			json.put("levelGroups", levelGroups.getLevelId());
		} else {
			json.put("levelGroups", "");
		}
		json.put("createBy", createBy);
		json.put("createDate", sdf.format(createDate));
		json.put("lastModifyBy", lastModifyBy);
		json.put("lastModifyDate", sdf.format(lastModifyDate));
		return json;
	}
}
