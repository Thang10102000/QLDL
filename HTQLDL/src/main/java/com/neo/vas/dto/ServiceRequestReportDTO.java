package com.neo.vas.dto;

import lombok.Data;
import org.json.JSONObject;

@Data
public class ServiceRequestReportDTO {
    private String service;
    private String brandGroup;
    private String brand;
    private String policy;
    private String price;
    private int priceDiscount;
    private int quantity;
    private int priceBrand;
    private int quantityActive;
    private int quantityNotActive;
    private int quantityRequestActive;
    private String activeDate;
    private int revenue;
    private Long serviceRequestId;
    private Long customerId;
    private String customerName;

    public JSONObject createJson(){
        JSONObject js = new JSONObject();
        js.put("service",service);
        js.put("brandGroup",brandGroup);
        js.put("brand",brand);
        js.put("policy",policy);
        js.put("price",price);
        js.put("priceDiscount",priceDiscount);
        js.put("quantity", quantity);
        js.put("priceBrand",priceBrand);
        js.put("quantityActive",quantityActive);
        js.put("quantityNotActive",quantityNotActive);
        js.put("quantityRequestActive",quantityRequestActive);
        js.put("activeDate",activeDate);
        js.put("revenue",revenue);
        js.put("serviceRequestId",serviceRequestId);
        js.put("customerId",customerId);
        js.put("customerName",customerName);
        return js;
    }
}
