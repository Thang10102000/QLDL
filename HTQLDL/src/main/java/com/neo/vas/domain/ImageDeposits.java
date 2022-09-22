/**
 * 
 */
package com.neo.vas.domain;

import java.util.Date;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

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
public class ImageDeposits {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ID_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_IMAGE_DEPOSITS", allocationSize = 1, name = "ID_SEQ")
	private long id;
	
	private String imagePath;
	@CreatedDate
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name = "deposits_id")
	private Deposits depositsID;
	
	@ManyToOne
	@JoinColumn(name = "agencyContract_id")
	private AgencyContract agencyContractID;

	@Override
	public String toString() {
		return "ImageDeposits [id=" + id + ", imagePath=" + imagePath + ", createdDate=" + createdDate + ", depositsID="
				+ depositsID + "]";
	}
	
}
