/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.SqlSummarize;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.SqlSummarizeModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class SqlSummarizeController extends TSFuncTemplate implements Serializable {

    private List<SqlSummarize> mlistApp;
    private SqlSummarize mtmpApp;
    private List<SqlSummarize> mselectedApp;
    private SqlSummarizeModel mmodel;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;

    public SqlSummarizeController() throws Exception {
        mmodel = new SqlSummarizeModel();
        mlistApp = mmodel.getListApp();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new SqlSummarize();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(SqlSummarize app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SqlSummarize) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(SqlSummarize app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (SqlSummarize) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleOK() throws Exception {
        if (isADD || isCOPY) {
            //Check permission
            if (!getPermission("I")) {
                return;
            }

            mmodel.add(mtmpApp);
            mlistApp.add(0, mtmpApp);

            //Reset form
            mtmpApp = new SqlSummarize();

            //Message to client
            ClientMessage.logAdd();

        } else if (isEDIT) {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            mmodel.edit(mtmpApp);
            mlistApp.set(selectedIndex, mtmpApp);

            //Message to client
            ClientMessage.logUpdate();
        }
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handleDelete() throws Exception {
        handleDelete(null);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void handleDelete(SqlSummarize ett) throws Exception {
        //Check permission
        if (!getPermission("D")) {
            return;
        }

        if (ett == null) {
            mmodel.delete(mselectedApp);

        } else {
            mmodel.delete(Collections.singletonList(ett));
        }


        mlistApp = mmodel.getListApp();
        mselectedApp = null;

        //Message to client
        ClientMessage.logDelete();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateView(SqlSummarize app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<SqlSummarize> getMlistApp() {
        return mlistApp;
    }

    public SqlSummarize getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(SqlSummarize mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<SqlSummarize> getMselectedApp() {
        return mselectedApp;
    }

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
    }
    public void setMselectedApp(List<SqlSummarize> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }
    public String getProfileCode(String input) {
        for(ProfilePartner partner: mlistProfilePartner) {
            if(partner.getProfileID().equals(input)) {
                return partner.getCode();
            }
        }
        return "";
    }
}
