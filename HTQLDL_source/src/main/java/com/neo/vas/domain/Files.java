/**
 * 
 */
package com.neo.vas.domain;

import java.util.Date;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
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
public class Files {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILES_SEQ")
	@SequenceGenerator(sequenceName = "SEQ_FILES", allocationSize = 1, name = "FILES_SEQ")
	private long id;

	private String filePath;
	private int fileSize;
	private String funcName;
	@CreatedBy
	private String createdBy;
	@CreatedDate
	private Date createdDate;
	private long referenceId;

	@Override
	public String toString() {
		return "Files{" +
				"id=" + id +
				", filePath='" + filePath + '\'' +
				", fileSize=" + fileSize +
				", funcName='" + funcName + '\'' +
				", createdBy='" + createdBy + '\'' +
				", createdDate=" + createdDate +
				'}';
	}
}
