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

/**
 * @author KhanhBQ
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Levels {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LV_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_LEVELS", allocationSize = 1, name = "LV_SEQ")
	private long levelId;

	private String levelName;

	@OneToMany(mappedBy = "levelFather", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Levels> childLevel = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_father_id")
	private Levels levelFather;

	@OneToMany(mappedBy = "levelUsers", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Users> usersLevel = new HashSet<>();

	@OneToMany(mappedBy = "levelGroups", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<Groupss> groupsLevel = new HashSet<>();

	@Override
	public String toString() {
		return "Levels [levelId=" + levelId + ", levelName=" + levelName + ", childLevel=" + childLevel
				+ ", levelFather=" + levelFather.getLevelName() + ", usersLevel=" + usersLevel + ", groupsLevel="
				+ groupsLevel + "]";
	}

}
