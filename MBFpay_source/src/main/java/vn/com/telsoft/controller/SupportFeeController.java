/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import org.apache.commons.lang.SerializationUtils;
import vn.com.telsoft.entity.Products;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.SupportFee;
import vn.com.telsoft.model.ProductsModel;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.SupportFeeModel;

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
public class SupportFeeController extends TSFuncTemplate implements Serializable {

    private List<SupportFee> mlistApp;
    private SupportFee mtmpApp;
    private List<SupportFee> mselectedApp;
    private SupportFeeModel mmodel;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;
    private ProfilePartner mselectedProfilePartner;
    private ProductsModel productsModel;
    private List<Products> productsList;

    public SupportFeeController() throws Exception {
        mmodel = new SupportFeeModel();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();
        mselectedProfilePartner = mlistProfilePartner.get(0);
        if (mselectedProfilePartner != null && !mselectedProfilePartner.getProfileID().isEmpty()) {
            mlistApp = mmodel.getListAppByProfileID(mselectedProfilePartner.getProfileID());
        } else {
            mlistApp = new ArrayList<>();
        }
        //mlistApp = mmodel.getListApp();
        productsModel = new ProductsModel();
        productsList = productsModel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new SupportFee();
        mtmpApp.setProfile_id(Integer.parseInt(mselectedProfilePartner.getProfileID()));
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(SupportFee app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SupportFee) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(SupportFee app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (SupportFee) SerializationUtils.clone(app);
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
            mtmpApp = new SupportFee();

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

    public void handleDelete(SupportFee ett) throws Exception {
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
    public void changeStateView(SupportFee app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<SupportFee> getMlistApp() {
        return mlistApp;
    }

    public SupportFee getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(SupportFee mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<SupportFee> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<SupportFee> mselectedApp) {
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

    public List<Products> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Products> productsList) {
        this.productsList = productsList;
    }

//    public String getProductName(String input) {
//        for (Products obj : productsList) {
//            if (obj.getProduct_code().equalsIgnoreCase(input)) {
//                return obj.getProduct_name();
//            }
//        }
//        return "";
//    }
}
