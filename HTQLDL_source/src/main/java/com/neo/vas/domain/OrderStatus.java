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
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OS_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_ORDER_DISCOUNT", allocationSize = 1, name = "OS_SEQ")
	private long id;

	private String statusName;

	@OneToMany(mappedBy = "orderStatusAO", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyOrders> agencyOrdersOS = new HashSet<>();
}
