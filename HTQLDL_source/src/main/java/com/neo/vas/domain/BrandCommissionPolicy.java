package com.neo.vas.domain;

import lombok.*;
import org.json.JSONObject;

import javax.persistence.*;
import java.text.SimpleDateFormat;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/26/2021
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BrandCommissionPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BCP_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_BRAND_COMMISSION_POLICY", allocationSize = 1, name = "BCP_SEQ")
    private long id;

    @ManyToOne
    @JoinColumn(name = "poc_id")
    private DiscountCommission discountCommissionBC;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brandBP;

    public JSONObject createJson() {
        JSONObject data = new JSONObject();
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        data.put("id",id);
        data.put("brandName", brandBP.getBrandName());
        data.put("policyName", discountCommissionBC.getPolicyName());
        data.put("startDate", ft.format(discountCommissionBC.getStartDate()));
        data.put("endDate", ft.format(discountCommissionBC.getEndDate()));
        data.put("commissionRate", discountCommissionBC.getRate());
        return data;
    }
}
