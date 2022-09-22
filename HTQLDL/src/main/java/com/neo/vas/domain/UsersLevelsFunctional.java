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
public class UsersLevelsFunctional {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ULF_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_USER_LEVELS_FUNCTIONAL", allocationSize = 1, name = "ULF_SEQ")
	private long ulfId;

	@ManyToOne
	@JoinColumn(name = "username")
	private Users usersULF;

	@ManyToOne
	@JoinColumn(name = "sf_id")
	private SystemFunctional systemFunctionalULF;

	@ManyToOne
	@JoinColumn(name = "privilege_id")
	private Privilegess privilegesULF;

	@Override
	public String toString() {
		return "UsersLevelsFunctional [ulfId=" + ulfId + ", usersULF=" + usersULF.getUsername()
				+ ", systemFunctionalULF=" + systemFunctionalULF.getSfName() + ", privilegesULF="
				+ privilegesULF.getPrivilegeName() + "]";
	}
}
