package com.neo.vas.domain;

import lombok.*;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgencyOrderRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AOR_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_AGENCY_ORDER_REQUEST", allocationSize = 1, name = "AOR_SEQ")
    private long id;

    @ManyToOne
    @JoinColumn(name = "ao_id")
    private AgencyOrders agencyOrders;

    @ManyToOne
    @JoinColumn(name = "sr_id")
    private ServiceRequest serviceRequest;

    public JSONObject creatẹJson() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        return json;
    }
}
