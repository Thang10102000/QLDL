package com.neo.vas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@AllArgsConstructor
@Data
public class AgencyAreaReportDTO {
    private String brandGrName;
    private String brandName;
    private String areaName;
    private String createdDate;
    private String endDate;
    private String approvedDate;
    private String approvedEndDate;
    private int reportMonth;

    private int sum_amount;//tong gia tri dat hang
    private int sum_revenue_brand_activated;//doanh thu goi da kich hoat
    private int sum_quantity_ck;//so don hang moi ck
    private int sum_quantity_new_ck;//so luong dat hang moi
    private int sum_quantity_brand_activated;//so luong goi da kich hoat
    private int sum_quantity_brand_unactivated;//so luong goi chua kich hoat
    private int sum_amount_ck;//tong gia tri dat hang ck
    private int sum_revenue_brand_acti_ck;//doanh thu goi da kich hoat ck
    private int agencyDiscount;// muc chiet khau dai ly

    private int sum_quantity_hh;// so don hang moi hh
    private int sum_quantity_new_hh;//so luong dat hang moi hh
    private int sum_quantity_brand_acti;//so luong goi da kich hoat hh
    private int sum_quantity_brand_unacti;//so luong goi chua kich hoat hh
    private int sum_amount_hh;// tong gia tri dat hang hh
    private int sum_revenue_brand_acti_hh;// doanh thu goi da kich hoat hh
    private int commission_rate;// muc chi phi hoa hong
    private int total_cost_of_service_commission;// tong chi phi hoa hong dich vu

    public AgencyAreaReportDTO() {

    }
    public JSONObject createJson(){
        JSONObject json = new JSONObject();
        json.put("brandGrName", brandGrName);
        json.put("brandName", brandName);
        json.put("areaName", areaName);
        json.put("createdDate", createdDate);
        json.put("endDate", endDate);
        json.put("approvedDate", approvedDate);
        json.put("approvedEndDate" ,approvedEndDate);
        json.put("reportMonth", reportMonth);
        json.put("sum_amount", sum_amount);
        json.put("sum_revenue_brand_activated", sum_revenue_brand_activated);
        json.put("sum_quantity_ck", sum_quantity_ck);
        json.put("sum_quantity_new_ck", sum_quantity_new_ck);
        json.put("sum_quantity_brand_activated", sum_quantity_brand_activated);
        json.put("sum_quantity_brand_unactivated", sum_quantity_brand_unactivated);
        json.put("sum_amount_ck", sum_amount_ck);
        json.put("sum_revenue_brand_acti_ck", sum_revenue_brand_acti_ck);
        json.put("agencyDiscount", agencyDiscount);
        json.put("sum_quantity_hh", sum_quantity_hh);
        json.put("sum_quantity_new_hh", sum_quantity_new_hh);
        json.put("sum_quantity_brand_acti", sum_quantity_brand_acti);
        json.put("sum_quantity_brand_unacti", sum_quantity_brand_unacti);
        json.put("sum_amount_hh", sum_amount_hh);
        json.put("sum_revenue_brand_acti_hh", sum_revenue_brand_acti_hh);
        json.put("commission_rate", commission_rate);
        json.put("total_cost_of_service_commission", total_cost_of_service_commission);
        return json;
    }
}
