/**
 * 
 */
package com.neo.vas.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author KhanhBQ
 *
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Privilegess {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRI_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_PRIVILEGES", allocationSize = 1, name = "PRI_SEQ")
	private long privilegeId;

	private String privilegeName;

	@OneToMany(mappedBy = "privilegesGLF")
	@JsonIgnore
	private Set<GroupsLevelsFunctional> groupsFunctionals = new HashSet<>();

	@OneToMany(mappedBy = "privilegesULF")
	@JsonIgnore
	private Set<UsersLevelsFunctional> usersFunctionals = new HashSet<>();
	
	@OneToMany(mappedBy = "privilegesUAH")
	@JsonIgnore
	private Set<UsersActionHistory> usersActionHistory = new HashSet<>();
}
