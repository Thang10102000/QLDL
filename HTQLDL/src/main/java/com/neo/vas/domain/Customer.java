package com.neo.vas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUS_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_CUSTOMER", allocationSize = 1, name = "CUS_SEQ")
    private long id;
    private String name;
    private String domain;
    private String email;
    private String phone;
//    private String school;
    private String address;
    private String province;
    private String district;
    private String agencyCode;
    private String wards;
    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Date createdDate;
    private Date updateDate;
    private String updateBy;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AgencyArea areaCId;

    @ManyToOne
    @JoinColumn(name="school_id")
    private School schoolId;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ServiceRequest> serviceRequests = new HashSet<>();

}
