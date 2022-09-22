package com.neo.vas.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * project_name: vasonline
 * Created_by: thulv
 * time: 11/05/2021
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgencyBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ABA_SEQ")
    @SequenceGenerator(sequenceName = "SEQ_AGENCY_BANK_ACCOUNT", allocationSize = 1, name = "ABA_SEQ")
    private long id;

    @ManyToOne
    @JoinColumn(name ="bank_account_id")
    private BankAccount bankAccountId;
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agencyId;

    public JSONObject createJson(){
        SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JSONObject json = new JSONObject();
        json.put("id",id);
        json.put("bankAccountId",bankAccountId.getId());
        json.put("bankAccountNo",bankAccountId.getBankAccountNo());
        json.put("bankName", (bankAccountId.getBankName() == null) ? "" : bankAccountId.getBankName());
        json.put("bankAddress",bankAccountId.getBankAddress());
        json.put("bankBranch",bankAccountId.getBankBranch());
        json.put("agencyBankAccounts",agencyId.getAgencyName());
        json.put("createdDate", (bankAccountId.getCreatedDate() == null) ? "" : fm.format(bankAccountId.getCreatedDate()));
        json.put("updateDate", (bankAccountId.getUpdateDate() == null) ? "" : fm.format(bankAccountId.getUpdateDate()));
        return json;
    }

}
