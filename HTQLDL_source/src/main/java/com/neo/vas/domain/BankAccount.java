package com.neo.vas.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * project_name: vasonline Created_by: thulv time: 11/05/2021
 */
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BA_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_BANK_ACCOUNT", allocationSize = 1, name = "BA_SEQ")
	private long id;
	private String bankAccountNo;
	private String bankName;
	private String bankAddress;
	private String bankBranch;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	private String updateBy;
	private Date updateDate;

	@OneToMany(mappedBy = "bankAccountId", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AgencyBankAccount> agencyBankAccounts = new HashSet<>();


}
