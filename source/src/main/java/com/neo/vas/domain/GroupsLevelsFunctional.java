/**
 * 
 */
package com.neo.vas.domain;

import javax.persistence.*;

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
public class GroupsLevelsFunctional {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLF_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_GROUPS_LEVELS_FUNCTIONAL", allocationSize = 1, name = "GLF_SEQ")
	private long glfId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gr_id")
	private Groupss groupsGLF;

	@ManyToOne
	@JoinColumn(name = "sf_id")
	private SystemFunctional systemFunctionalGLF;

	@ManyToOne
	@JoinColumn(name = "privilege_id")
	private Privilegess privilegesGLF;

	@Override
	public String toString() {
		return "GroupsLevelsFunctional [glfId=" + glfId + ", groupsGLF=" + groupsGLF.getGroupId() + ", systemFunctionalGLF="
				+ systemFunctionalGLF.getSfName() + ", privilegesGLF=" + privilegesGLF.getPrivilegeName() + "]";
	}

}
