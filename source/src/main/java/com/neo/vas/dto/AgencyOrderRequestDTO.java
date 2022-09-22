package com.neo.vas.dto;

import com.neo.vas.domain.Brand;
import com.neo.vas.domain.Customer;
import com.neo.vas.domain.ServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgencyOrderRequestDTO {
    private long srId;
    private Customer customer;
    private Brand brandASR;
    private long status;
    private String policy;
    private long price;
    private long discountCost;
    private long quantity;
    private Integer amount;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date approved;
    @CreatedBy
    private String creator;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date created;
    @LastModifiedBy
    private String modifier;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date modified;
    private Integer remainingQuantity;
    private int type;

    public AgencyOrderRequestDTO setValueDTO(ServiceRequest sr){
        AgencyOrderRequestDTO aR = new AgencyOrderRequestDTO();
        aR.setSrId(sr.getSrId());
        aR.setCustomer(sr.getCustomer());
        aR.setBrandASR(sr.getBrandASR());
        aR.setStatus(sr.getStatus());
        aR.setPolicy(sr.getPolicy());
        aR.setPrice(sr.getPrice());
        aR.setDiscountCost(sr.getDiscountCost());
        aR.setQuantity(sr.getQuantity());
        aR.setAmount(sr.getAmount());
        aR.setApproved(sr.getApproved());
        aR.setCreator(sr.getCreator());
        aR.setCreated(sr.getCreated());
        aR.setModified(sr.getModified());
        aR.setModifier(sr.getModifier());
        aR.setRemainingQuantity(sr.getRemainingQuantity());
        return aR;
    }
}
