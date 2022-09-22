package com.neo.vas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/26/2021
 */
@AllArgsConstructor
@Data
public class AgencyContractDTO {
    private String agencyName;
    private String contractNo;
    private String serviceType;
    private Date signDate;
    private Date startDate;
    private Date endDate;
    private long guaranteeValue;
    private int status;
    private int liquidationDate;
    private String areaName;

    public JSONObject createJson() {
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject json = new JSONObject();
        json.put("agencyName", agencyName);
        json.put("contractNo", contractNo);
        json.put("serviceType", serviceType);
        json.put("signDate", ft.format(signDate));
        json.put("startDate", ft.format(startDate));
        json.put("endDate", ft.format(endDate));
        json.put("guaranteeValue", guaranteeValue);
        json.put("liquidationDate", liquidationDate);
        json.put("status", status);
        json.put("areaName", areaName);
        return json;
    }
}
