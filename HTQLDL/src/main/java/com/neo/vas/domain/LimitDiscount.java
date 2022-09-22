package com.neo.vas.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/21/2021
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LimitDiscount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LD_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_LIMIT_DISCOUNT", allocationSize = 1, name = "LD_SEQ")
    private Long id;
    private Long minOrder;
    private Long limitOrder;
    private int discountRate;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    private String updateBy;
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private DiscountCommission discountCommission;

    public JSONObject createJson(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("minOrder",minOrder);
        json.put("limitOrder",limitOrder);
        json.put("discountRate",discountRate);
        return json;
    }
}
