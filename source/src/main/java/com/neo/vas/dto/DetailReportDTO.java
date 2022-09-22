package com.neo.vas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@AllArgsConstructor
@Data
public class DetailReportDTO {
    private String customerName;
    private long customerId;
    private long serviceRequestId;
    private String areaName;
    private String agencyCode;
    private String amCode;
    private int agencyType;
    private String createdDate;
    private String endDate;
    private String approvedDate;
    private String dateEnd;
    private int reportMonth;
    private String creator;
    private String brandGroupName;
    private String brandName;
    private long brandId;
    private int price;
    private int quantity;
    private int rowNum; // sinh ma tu dong
    private String asrCreatedDate;
    private int quantityActivations;
    private int amount;
    private int discountAgency;
    private int costAmKam;
    private int quantityUnactivated;
    private int amountUnactivated;

    public DetailReportDTO() {

    }

    public JSONObject createJson(){
        JSONObject json = new JSONObject();
        json.put("customerName", customerName);
        json.put("customerId", customerId);
        json.put("serviceRequestId", serviceRequestId);
        json.put("areaName", areaName);
        json.put("agencyCode", agencyCode);
        json.put("amCode" ,amCode);
        json.put("createdDate",createdDate);
        json.put("endDate", endDate);
        json.put("approvedDate", approvedDate);
        json.put("dateEnd", dateEnd);
        json.put("reportMonth", reportMonth);
        json.put("creator", creator);
        json.put("brandGroupName", brandGroupName);
        json.put("brandName", brandName);
        json.put("brandId", brandId);
        json.put("price", price);
        json.put("quantity", quantity);
        json.put("rowNum", rowNum);
        json.put("asrCreatedDate",asrCreatedDate);
        json.put("quantityActivations", quantityActivations);
        json.put("amount", amount);
        json.put("discountAgency", discountAgency);
        json.put("costAmKam", costAmKam);
        json.put("quantityUnactivated", quantityUnactivated);
        json.put("amountUnactivated", amountUnactivated);
        return json;
    }
}
