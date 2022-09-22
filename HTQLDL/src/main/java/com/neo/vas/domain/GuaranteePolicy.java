package com.neo.vas.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GuaranteePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GP_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_GUARANTEE_POLICY", allocationSize = 1, name = "GP_SEQ")
    private Long id;
    private int limit;
    private String createdBy;
    private Date createdDate;
    private String updateBy;
    private Date updateDate;
}
