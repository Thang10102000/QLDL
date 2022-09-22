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
public class AgencyInformationDTO {
    private Long idRepresent;
    private String fullNameRepresent;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdayRepresent;

    private String positionRepresent;
    private String emailRepresent;
    private String telephoneRepresent;
    private String phoneNumberRepresent;
}
