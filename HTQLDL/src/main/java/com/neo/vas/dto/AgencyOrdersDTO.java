package com.neo.vas.dto;

import com.neo.vas.domain.AgencyOrders;
import lombok.*;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class AgencyOrdersDTO {
    private AgencyOrders agencyOrders;
    private long remainingAmount;

    public JSONObject createJson(){
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat ftTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject json = new JSONObject();
        json.put("orderId",agencyOrders.getOrderId());
        json.put("orderValue",agencyOrders.getOrderValue());
        json.put("orderPay",agencyOrders.getOrderPay());
        json.put("description",agencyOrders.getDescription());
        json.put("paymentMethod",agencyOrders.getPaymentMethod());
        json.put("currentValue", (agencyOrders.getCurrentValue() == null) ? 0 : agencyOrders.getCurrentValue());
        json.put("startDate",ft.format(agencyOrders.getStartDate()));
        json.put("endDate",ft.format(agencyOrders.getEndDate()));
        json.put("agencyAO",agencyOrders.getAgencyAO().getAgencyName());
        json.put("orderStatusAO",agencyOrders.getOrderStatusAO().getId());
        json.put("orderStatusAOName",agencyOrders.getOrderStatusAO().getStatusName());
        json.put("consumptionAmount",(agencyOrders.getConsumptionAmount() == null) ? 0 : agencyOrders.getConsumptionAmount());
        json.put("remainingAmount",remainingAmount);
        json.put("createdDate",(agencyOrders.getCreatedDate() == null) ? "" : ftTime.format(agencyOrders.getCreatedDate()));
        json.put("updateDate",(agencyOrders.getUpdatedDate() == null) ? "" : ftTime.format(agencyOrders.getUpdatedDate()));
        json.put("agencyArea",agencyOrders.getAgencyAO().getAreaId().getAreaName());
        return json;
    }
}
