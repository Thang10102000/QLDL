package com.neo.vas.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * project_name: vasonline
 * author: thuluuvan
 * time: 7/30/2021
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AgencyDCHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADCH_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_AGENCY_DC_HISTORY", allocationSize = 1, name = "ADCH_SEQ")
    private long id;
    private long agencyId;
    private long discountCommissionId;
//    0: chinh sách hoa hồng, 1: chính sách chiết khấu
    private int type;
    private Date createdDate;
    private String createdBy;
}
