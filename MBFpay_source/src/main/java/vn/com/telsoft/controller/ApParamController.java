/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.ApParam;
import vn.com.telsoft.model.ApParamModel;

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
public class ApParamController extends TSFuncTemplate implements Serializable {

    private List<ApParam> mlistApp;
    private ApParam mtmpApp;
    private List<ApParam> mselectedApp;
    private ApParamModel mmodel;

    public ApParamController() throws Exception {
        mmodel = new ApParamModel();
        mlistApp = mmodel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new ApParam();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(ApParam app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (ApParam) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(ApParam app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (ApParam) SerializationUtils.clone(app);
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
            mtmpApp = new ApParam();

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

    public void handleDelete(ApParam ett) throws Exception {
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
    public void changeStateView(ApParam app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<ApParam> getMlistApp() {
        return mlistApp;
    }

    public ApParam getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(ApParam mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<ApParam> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<ApParam> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }
}
