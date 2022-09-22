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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SCHOOL_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_SCHOOL", allocationSize = 1, name = "SCHOOL_SEQ")
    private long id;

    private String schoolCode;
    private String schoolType;
    private String description;

    @CreatedBy
    private String createdBy;
    @CreatedDate
    private Date createdDate;
    private Date updateDate;
    private String updateBy;

    @OneToMany(mappedBy = "schoolId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();

    public JSONObject createJson(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("schoolCode", schoolCode);
        json.put("schoolType", schoolType);
        json.put("description", description);
        json.put("createdDate", (createdDate == null) ? "" : dateFormat.format(createdDate));
        json.put("updateDate", (updateDate == null) ? "" : dateFormat.format(updateDate));
        return json;
    }

}
