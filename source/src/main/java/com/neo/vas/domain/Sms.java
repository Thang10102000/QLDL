/**
 * 
 */
package com.neo.vas.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class Sms {
	@Id
	private String id;
	private String content;
	private String smsTemp;
	private String referenceInfo;
	private int length;
	private int queueId;
	private Date createdTime;
	private String mobile;
	private String mobile1;
}
