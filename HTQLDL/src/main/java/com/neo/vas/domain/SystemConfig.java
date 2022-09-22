/**
 * 
 */
package com.neo.vas.domain;

import javax.persistence.*;

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
public class SystemConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SC_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_SYSTEM_CONFIG", allocationSize = 1, name = "SC_SEQ")
	private long sId;

	private int maxLogin;
	private int maxLoginFail;
	private int minPwLength;
	private int pwExpiredDate;
}
