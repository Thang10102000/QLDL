/**
 * 
 */
package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

/**
 * @author KhanhBQ
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SystemFunctional {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SF_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_SYSTEM_FUNCTIONAL", allocationSize = 1, name = "SF_SEQ")
	private long sfId;
	private Integer status;
	private String sfName;
	private String urlController;
	private String fontAwesomeIconClass;

	@OneToMany(mappedBy = "systemFunctionalGLF")
	@JsonIgnore
	private Set<GroupsLevelsFunctional> groupsFunctionals = new HashSet<>();

	@OneToMany(mappedBy = "systemFunctionalULF")
	@JsonIgnore
	private Set<UsersLevelsFunctional> usersFunctionals = new HashSet<>();

	@OneToMany(mappedBy = "sfFather")
	@JsonIgnore
	private Set<SystemFunctional> childMenu = new HashSet<>();

	@ManyToOne
	@JoinColumn(name = "menu_father_id")
	private SystemFunctional sfFather;
	
	@OneToMany(mappedBy = "systemFunctionalUAH")
	@JsonIgnore
	private Set<UsersActionHistory> usersActionHistory = new HashSet<>();

	@OneToMany(mappedBy = "module")
	@JsonIgnore
	private Set<LogAPI> sfModule = new HashSet<>();

	@Override
	public String toString() {
		return "SystemFunctional [sfId=" + sfId + ", sfName=" + sfName + ", urlController=" + urlController
				+ ", groupsFunctionals=" + groupsFunctionals + ", usersFunctionals=" + usersFunctionals + ", childMenu="
				+ childMenu + ", sfFather=" + ((sfFather != null) ? sfFather.getSfName() : "null") + ", status="+ status +  "]";
	}

	public JSONObject createJson() {
		JSONObject json = new JSONObject();
		json.put("sfId", sfId);
		json.put("fontAwesomeIconClass", fontAwesomeIconClass);
		json.put("sfName", sfName);
		json.put("status", status);
		json.put("urlController", (urlController == null) ? "" : urlController);
		json.put("sfFather", sfFather != null ? sfFather.getSfName() : null);
		return json;
	}
}
