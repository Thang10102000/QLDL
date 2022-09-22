package com.neo.vas.domain;

import lombok.*;
import org.json.JSONObject;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "api_Log")
public class LogAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOG_API_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_API_LOG", allocationSize = 1, name = "LOG_API_SEQ")
    private long alId;

//    @Column
//    private String module;
    @Column
    private String request;
    @Column
    private String response;
    private String url;
    @Column
    private Date created;
    @Column
    private String creator;
    private Long referenceId;
    private int status;

    @ManyToOne
    @JoinColumn(name = "module")
    private SystemFunctional module;

    public JSONObject createJson(){
        JSONObject json = new JSONObject();
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        json.put("alId", alId);
        json.put("module", module.getSfName());
        json.put("request", request);
        json.put("response", response);
        json.put("created", ft.format(created));
        json.put("creator", creator);
        json.put("status", status);
        json.put("referenceId", referenceId);
        return json;
    }
}
