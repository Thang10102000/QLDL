package com.neo.vas.domain;

import lombok.*;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author YNN
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "type", "name" }) })
public class SystemAttr {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SYSTEM_ATTR_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_SYSTEM_ATTR", allocationSize = 1, name = "SYSTEM_ATTR_SEQ")
    private long saId;

    private String type;

    private String name;

    private String value;

    private String description;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date modified;

    private String modifier;

    @Override
    public String toString() {
        return "Levels [saId=" + saId + ", type=" + type + ", name=" + name
                + ", value=" + value + ", description=" + description + ", modified="
                + modified + "]";
    }

    public JSONObject createJson() {
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject json = new JSONObject();
        json.put("saId", saId);
        json.put("type", (type==null) ? "" : type);
        json.put("name", (name==null) ? "" : name);
        json.put("value", (value==null) ? "" : value);
        json.put("description", (description==null) ? "" : description);
        json.put("modified", (modified == null) ? "" : fm.format(modified));
        json.put("modifier", modifier);
        return json;
    }
}
