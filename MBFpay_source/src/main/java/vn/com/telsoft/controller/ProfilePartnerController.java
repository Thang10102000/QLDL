/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.ProfilePartnerModel;

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
public class ProfilePartnerController extends TSFuncTemplate implements Serializable {

    private List<ProfilePartner> mlistApp;
    private ProfilePartner mtmpApp;
    private List<ProfilePartner> mselectedApp;
    private ProfilePartnerModel mmodel;

    public ProfilePartnerController() throws Exception {
        mmodel = new ProfilePartnerModel();
        mlistApp = mmodel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new ProfilePartner();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(ProfilePartner app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (ProfilePartner) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(ProfilePartner app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (ProfilePartner) SerializationUtils.clone(app);
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
            mtmpApp = new ProfilePartner();

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

    public void handleDelete(ProfilePartner ett) throws Exception {
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

    public void changeStateView(ProfilePartner app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<ProfilePartner> getMlistApp() {
        return mlistApp;
    }

    public ProfilePartner getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(ProfilePartner mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<ProfilePartner> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<ProfilePartner> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }
}
