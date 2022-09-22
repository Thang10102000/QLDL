/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.com.telsoft.entity.PartnerFee;
import vn.com.telsoft.entity.Products;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.model.PartnerFeeModel;
import vn.com.telsoft.model.ProductsModel;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author ThongNM
 */

@Named
@ViewScoped
public class PartnerFeeController extends TSFuncTemplate implements Serializable {

    private List<PartnerFee> mlistApp;
    private PartnerFee mtmpApp;
    private List<PartnerFee> mselectedApp;
    private PartnerFeeModel mmodel;
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;
    private ProfilePartner mselectedProfilePartner;
    private ProductsModel productsModel;
    private List<Products> productList;

    //Excel
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "template_partner_fee.xlsx";
    private final String strPrefexFileName = "PartnerFee_";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private StreamedContent file;

    public PartnerFeeController() throws Exception {
        mmodel = new PartnerFeeModel();
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
        productList = productsModel.getListApp();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new PartnerFee();
        mtmpApp.setProfileID(Integer.parseInt(mselectedProfilePartner.getProfileID()));
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(PartnerFee app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (PartnerFee) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(PartnerFee app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (PartnerFee) SerializationUtils.clone(app);
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
            mtmpApp = new PartnerFee();

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

    public void handleDelete(PartnerFee ett) throws Exception {
        //Check permission
        if (!getPermission("D")) {
            return;
        }

        if (ett == null) {
            mmodel.delete(mselectedApp);
        } else {
            mmodel.delete(Collections.singletonList(ett));
        }


        mlistApp = mmodel.getListAppByProfileID(ett.getProfileID()+"");
        mselectedApp = null;

        //Message to client
        ClientMessage.logDelete();
    }

    //////////////////////////////////////////////////////////////////////////////////
    public void changeStateView(PartnerFee app) throws Exception {
        changeStateEdit(app);
        super.changeStateView();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<PartnerFee> getMlistApp() {
        return mlistApp;
    }

    public PartnerFee getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(PartnerFee mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<PartnerFee> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<PartnerFee> mselectedApp) {
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

    public List<Products> getProductList() {
        return productList;
    }

    public void setProductList(List<Products> productList) {
        this.productList = productList;
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = strPrefexFileName + DateFormatUtils.format(new Date(), "yyyyMMdd") +
                ".xls";
        String destFileName = strFoderExport + File.separator + fileName;
        FileUtil.forceFolderExist(strFoderExport);
        Map beans = new HashMap();
        beans.put("app", mlistApp);
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(templateFileName, beans, destFileName);
        file = new DefaultStreamedContent(new FileInputStream(destFileName), "application/xls",
                fileName);
        return file;
    }
}
