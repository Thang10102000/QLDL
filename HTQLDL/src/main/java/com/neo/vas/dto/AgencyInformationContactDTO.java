package com.neo.vas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/21/2021
 */
@Data
@AllArgsConstructor
public class AgencyInformationContactDTO {
    private Long idContact;
    private String fullNameContact;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdayContact;

    private String positionContact;
    private String emailContact;
    private String telephoneContact;
    private String phoneNumberContact;
}
