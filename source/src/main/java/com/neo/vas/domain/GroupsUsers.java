/**
 * 
 */
package com.neo.vas.domain;

import javax.persistence.*;

import org.json.JSONObject;

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
public class GroupsUsers {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GU_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_GROUPS_USER", allocationSize = 1, name = "GU_SEQ")
	private long guId;

	@ManyToOne
	@JoinColumn(name = "username")
	private Users usersGU;

	@ManyToOne
	@JoinColumn(name = "group_id")
	private Groupss groupIdGU;

	@Override
	public String toString() {
		return "GroupsUsers [guId=" + guId + ", usersGU=" + usersGU.getUsername() + ", groupIdGU="
				+ groupIdGU.getGroupName() + "]";
	}

	public JSONObject createJson() {
		JSONObject data = new JSONObject();
		data.put("guId", guId);
		if (usersGU != null) {
			data.put("usersGU", usersGU.createJson());
		} else {
			data.put("usersGU", "");
		}
		if (groupIdGU != null) {
			data.put("groupIdGU", groupIdGU.getGroupName());
		} else {
			data.put("groupIdGU", "");
		}
		return data;
	}

}
