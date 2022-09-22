package com.neo.vas.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AgencyArea {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AA_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_AGENCY_AREA", allocationSize = 1, name = "AA_SEQ")
    private long areaId;
    private String areaName;
    private String areaCode;
    private String description;
    @CreatedBy
    private String createBy;
    @CreatedDate
    private Date createDate;
    private String updateBy;
    private Date updateDate;
    private String taxCode;

    @OneToMany(mappedBy = "areaId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Agency> agencies = new HashSet<>();
    
    @OneToMany(mappedBy = "areaId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Users> users = new HashSet<>();
    @OneToMany(mappedBy = "areaCId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    public JSONObject jsonObject()
    {
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject json = new JSONObject();
        json.put("areaId",areaId);
        json.put("areaCode",(areaCode == null) ? "" : areaCode);
        json.put("areaName",areaName);
        json.put("description",(description==null) ? "" : description);
        json.put("createdDate", (createDate == null) ? "" : fm.format(createDate));
        json.put("updateDate", (updateDate == null) ? "" : fm.format(updateDate));
        json.put("taxCode", (taxCode == null) ? "" :  taxCode);
        return json;
    }
}
