package com.neo.vas.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequestActivate {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SRA_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_SERVICE_REQUEST_ACTIVATE", allocationSize = 1, name = "SRA_SEQ")
	private long sraId;
	private Integer quantity;
	private String orderCode;
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date activated;
	private String activator;
	private Integer remaining;
	private Integer status;
	private Long amount;
	private Long orderId;
	
	@ManyToOne
	@JoinColumn(name = "sr_id")
	private ServiceRequest serviceRequest;
}
