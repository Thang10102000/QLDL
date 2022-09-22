package com.neo.vas.domain;

/*
 ** @project_name: vasonline
 ** @author: ThuLv
 ** @created_date: 12/21/2021
 */

import lombok.*;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiTransactionOrderLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AOL_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_API_ORDER_LOG", allocationSize = 1, name = "AOL_SEQ")
    private long logId;
    private String url;
    private String request;
    private String response;
    @CreatedBy
    private String creator;
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date created;
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date responseTime;

    public JSONObject createJson(){
        JSONObject json = new JSONObject();
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        json.put("logId", logId);
        json.put("created", ft.format(created));
        json.put("creator", creator);
        json.put("response", (response == null) ? "" : response);
        json.put("request",request);
        json.put("responseTime", responseTime == null ? "" : ft.format(responseTime));
        json.put("url",url);
        return json;
    }
}
