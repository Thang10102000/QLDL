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
public class AgencyInformationAuthorizedDTO {
    private Long idAuthorized;
    private String fullNameAuthorized;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthdayAuthorized;

    private String positionAuthorized;
    private String emailAuthorized;
    private String telephoneAuthorized;
    private String phoneNumberAuthorized;
}
