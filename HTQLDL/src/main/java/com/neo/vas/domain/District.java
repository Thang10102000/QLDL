package com.neo.vas.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class District {
    @Id
    private long id;
    private String districtName;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province provinceId;

    @OneToMany(mappedBy = "districtId", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ward> wards = new HashSet<>();
}
