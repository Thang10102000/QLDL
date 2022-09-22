/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.SourceData;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.SourceDataModel;

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
public class SourceDataController extends TSFuncTemplate implements Serializable {

    private List<SourceData> mlistApp;
    private SourceData mtmpApp;
    private List<SourceData> mselectedApp;
    private SourceDataModel mmodel;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;

    public SourceDataController() throws Exception {
        mmodel = new SourceDataModel();
        mlistApp = mmodel.getListApp();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new SourceData();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(SourceData app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SourceData) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(SourceData app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (SourceData) SerializationUtils.clone(app);
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
            mtmpApp = new SourceData();

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

    public void handleDelete(SourceData ett) throws Exception {
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

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<SourceData> getMlistApp() {
        return mlistApp;
    }

    public SourceData getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(SourceData mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<SourceData> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<SourceData> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
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
