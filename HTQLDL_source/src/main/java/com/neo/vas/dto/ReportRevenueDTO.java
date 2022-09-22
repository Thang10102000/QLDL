package com.neo.vas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRevenueDTO {

    private String serviceName;
    private String agencyCode;
    private String brandGroupName;
    private String brandName;
    private int quantity;
    private int brandNew;
    private int brandEt;
    private int brandOff;
    private Long totalMoney;
    private int countBrand;
    private int discountRate;
    // ---search
    private String createdDate;
    private String endDate;
    private String approvedDate;
    private String fromDate;
    private String brandId;
    private String brandGroupId;
    private String areaId;
    private String costAmKam;
    private int amountUnactivated;


    public JSONObject createJson(){
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        JSONObject json = new JSONObject();
        json.put("serviceName", serviceName);
        json.put("agencyCode", agencyCode);
        json.put("brandGroupName", brandGroupName);
        json.put("brandName", brandName);
        json.put("quantity" ,quantity);
        json.put("brandNew", brandNew);
        json.put("brandEt", brandEt);
        json.put("brandOff", brandOff);
        json.put("totalMoney", totalMoney);
        json.put("countBrand", countBrand);
        json.put("discountRate", discountRate);
        json.put("approvedDate", approvedDate);
        json.put("brandId", brandId);
        json.put("brandGroupId", brandGroupId);
        json.put("areaId", areaId);
        json.put("createdDate", createdDate);
        json.put("fromDate", fromDate);
        json.put("endDate", endDate);
        json.put("costAmKam", costAmKam);
        json.put("amountUnactivated", amountUnactivated);
        return json;
    }
}
