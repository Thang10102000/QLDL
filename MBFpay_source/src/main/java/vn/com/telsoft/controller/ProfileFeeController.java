/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.PayType;
import vn.com.telsoft.entity.ProfileFee;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.PayTypeModel;
import vn.com.telsoft.model.ProfileFeeModel;
import vn.com.telsoft.model.ProfilePartnerModel;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author NOINV
 */
@Named
@ViewScoped
public class ProfileFeeController extends TSFuncTemplate implements Serializable {

    private List<ProfileFee> mlistApp;
    private ProfileFee mtmpApp;
    private List<ProfileFee> mselectedApp;
    private ProfileFeeModel mmodel;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;
    private ProfilePartner mselectedProfilePartner;
    private PayTypeModel payTypeModel;
    private List<PayType> payTypeList;

    public ProfileFeeController() throws Exception {
        mmodel = new ProfileFeeModel();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();
        mselectedProfilePartner = mlistProfilePartner.get(0);
        if (mselectedProfilePartner != null && !mselectedProfilePartner.getProfileID().isEmpty()) {
            mlistApp = mmodel.getListAppByProfileID(mselectedProfilePartner.getProfileID());
        } else {
            mlistApp = new ArrayList<>();
        }
        //mlistApp = mmodel.getListApp();
        payTypeModel = new PayTypeModel();
        payTypeList = payTypeModel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new ProfileFee();
        mtmpApp.setProfile_id(Integer.parseInt(mselectedProfilePartner.getProfileID()));
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(ProfileFee app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (ProfileFee) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(ProfileFee app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (ProfileFee) SerializationUtils.clone(app);
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
            mtmpApp = new ProfileFee();

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

    public void handleDelete(ProfileFee ett) throws Exception {
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
    public void changeStateView(ProfileFee app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<ProfileFee> getMlistApp() {
        return mlistApp;
    }

    public ProfileFee getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(ProfileFee mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<ProfileFee> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<ProfileFee> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
    }

    public String getProfileCode(String input) {
        for (ProfilePartner partner : mlistProfilePartner) {
            if (partner.getProfileID().equals(input)) {
                return partner.getCode();
            }
        }
        return "";
    }

    public ProfilePartner getMselectedProfilePartner() {
        return mselectedProfilePartner;
    }

    public void setMselectedProfilePartner(ProfilePartner mselectedProfilePartner) {
        this.mselectedProfilePartner = mselectedProfilePartner;
    }

    public void onRowSelect() throws Exception {
        if (mselectedProfilePartner != null && !mselectedProfilePartner.getProfileID().isEmpty()) {
            mlistApp = mmodel.getListAppByProfileID(mselectedProfilePartner.getProfileID());
        } else {
            mlistApp = new ArrayList<>();
        }
    }

    public List<PayType> getPayTypeList() {
        return payTypeList;
    }

    public void setPayTypeList(List<PayType> payTypeList) {
        this.payTypeList = payTypeList;
    }

    public String getPayTypeName(int input) {
        for (PayType obj : payTypeList) {
            if (obj.getPay_type() == input) {
                return obj.getPay_type_name();
            }
        }
        return "";
    }
}
