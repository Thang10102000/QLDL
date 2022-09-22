package com.neo.vas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/21/2021
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DiscountCommission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DC_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_DISCOUNT_COMMISSION", allocationSize = 1, name = "DC_SEQ")
    private Long id;
    private String policyName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date endDate;

    private int rate;
    private int type;
    private int isDefault;
    private String description;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Date createdDate;
    private String updateBy;
    private Date updateDate;


    @OneToMany(mappedBy = "discountCommissionBC", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<BrandCommissionPolicy> discountCommissionBC = new HashSet<>();

    @OneToMany(mappedBy = "discountPolicyAD", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<AgencyDiscountCommission> agencyDiscounts = new HashSet<>();

    @OneToMany(mappedBy = "discountCommission", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<LimitDiscount> limitDiscounts = new HashSet<>();

    public JSONObject createJson(){
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat fmT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("policyName",policyName);
        json.put("startDate",ft.format(startDate));
        json.put("endDate",ft.format(endDate));
        json.put("rate",rate);
        json.put("description",(description == null) ? "" : description);
        json.put("createdBy",createdBy);
        json.put("createdDate", (createdDate == null) ? "" : fmT.format(createdDate));
        json.put("updateDate",(updateDate == null) ? "" : fmT.format(updateDate));

        return json;
    }

}
