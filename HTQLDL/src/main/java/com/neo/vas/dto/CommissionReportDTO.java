package com.neo.vas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@AllArgsConstructor
@Data
public class CommissionReportDTO {
    private String serviceName;
    private String agencyCode;//maamkam
    private String brandGrName;
    private String brandName;
    private String areaName;
    private String agencyName;
    private int quantityNewOrder;//số lượng đơn hàng mới
    private int quantityNewBrand;// số lượng gói cước mới yêu cầu
    private int quantityActivationsBrand;// số lượng gói đã kích hoạt
    private int quantityUnactivatedBrand;// số lượng gói chưa kích hoạt
    private int quantityServiceRequestActivate; // số lương gói da kich hoat khong trong ki
    private int revenueBrand;// doanh thu gói đã kích hoạt
    private int commissionRate;// mức chi phí hoa hồng
    private int totalCost; // tong chi phi hoa hong dich vu
    private String createdDate;
    private String endDate;
    private String approvedDate;
    private String approvalEndDate;
    private int reportMonth;

    public CommissionReportDTO() {

    }

    public JSONObject createJson() {
        JSONObject json = new JSONObject();
        json.put("serviceName", serviceName);
        json.put("agencyCode", agencyCode);
        json.put("brandGrName", brandGrName);
        json.put("brandName", brandName);
        json.put("areaName", areaName);
        json.put("agencyName", agencyName);
        json.put("quantityNewOrder", quantityNewOrder);
        json.put("quantityNewBrand", quantityNewBrand);
        json.put("quantityActivationsBrand", quantityActivationsBrand);
        json.put("quantityUnactivatedBrand", quantityUnactivatedBrand);
        json.put("quantityServiceRequestActivate", quantityServiceRequestActivate);
        json.put("revenueBrand", revenueBrand);
        json.put("commissionRate", commissionRate);
        json.put("totalCost", totalCost);
        json.put("createdDate", createdDate);
        json.put("endDate", endDate);
        json.put("approvedDate", approvedDate);
        json.put("approvalEndDate", approvalEndDate);
        json.put("reportMonth", reportMonth);
        return json;
    }
}
