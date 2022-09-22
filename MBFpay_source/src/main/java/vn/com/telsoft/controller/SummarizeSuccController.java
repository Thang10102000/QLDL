/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.com.telsoft.controller;

import com.faplib.lib.ClientMessage;
import com.faplib.lib.SystemLogger;
import com.faplib.lib.TSFuncTemplate;
import com.faplib.util.FileUtil;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import vn.com.telsoft.entity.ProfilePartner;
import vn.com.telsoft.entity.SummarizeSucc;
import vn.com.telsoft.model.ProfilePartnerModel;
import vn.com.telsoft.model.SummarizeSuccModel;
import vn.com.telsoft.util.JsfConstant;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author YNN
 */
@Named
@ViewScoped
public class SummarizeSuccController extends TSFuncTemplate implements Serializable {

    private List<SummarizeSucc> mlistApp;
    private SummarizeSucc mtmpApp;
    private List<SummarizeSucc> mselectedApp;
    private SummarizeSuccModel mmodel;
    private StreamedContent file;
    private final String strRealPath = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_TEMPLATE);
    private final String strFileTemplate = "template_matched_transaction.xlsx";
    private final String strFoderExport = FileUtil.getRealPath(JsfConstant.TEMP_FOLDER_FILE_EXPORT);
    private final String strPrefexFileName = "Matched_";
    private ProfilePartnerModel mprofilePartnerModel;
    private List<ProfilePartner> mlistProfilePartner;
    private String mProfileID;
    private Date mdStartDate;
    private Date mdEndDate;


    public SummarizeSuccController() throws Exception {
        mmodel = new SummarizeSuccModel();
        mlistApp = new ArrayList<>();
        mprofilePartnerModel = new ProfilePartnerModel();
        mlistProfilePartner = mprofilePartnerModel.getListApp();
        mdStartDate = new Date();
        mdEndDate = new Date();
    }
    //////////////////////////////////////////////////////////////////////////////////

    @Override
    public void changeStateAdd() throws Exception {
        super.changeStateAdd();
        mtmpApp = new SummarizeSucc();
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateEdit(SummarizeSucc app) throws Exception {
        super.changeStateEdit();
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SummarizeSucc) SerializationUtils.clone(app);
    }
    //////////////////////////////////////////////////////////////////////////////////

    public void changeStateCopy(SummarizeSucc app) throws Exception {
        super.changeStateCopy();
        mtmpApp = (SummarizeSucc) SerializationUtils.clone(app);
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
            mtmpApp = new SummarizeSucc();

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

    public void handleDelete(SummarizeSucc ett) throws Exception {
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

    public void handleSearch() throws Exception {
        try {
            // System.out.printf("(mProfileID, mdStartDate, mdEndDate) : (%s, %s, %s)", mProfileID, mdStartDate, mdEndDate);
            mlistApp = mmodel.getListApp2(mProfileID, mdStartDate, mdEndDate);
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }

    public void changeStateProcess(SummarizeSucc app) throws Exception {
        selectedIndex = mlistApp.indexOf(app);
        mtmpApp = (SummarizeSucc) SerializationUtils.clone(app);
    }

    public void handleProcess(String input) throws Exception {
        try {
            //Check permission
            if (!getPermission("U")) {
                return;
            }

            Date dtExecute = new Date();
            mtmpApp.setStatus(input);
            mlistApp.set(selectedIndex, mtmpApp);


            //Message to client
            ClientMessage.logUpdate();
        } catch (Exception var2) {
            SystemLogger.getLogger().error(var2, var2);
            ClientMessage.logErr(ClientMessage.MESSAGE_TYPE.ERR, var2.toString());
        }
    }


    public boolean isIsSelectedApp() {
        return mselectedApp != null && !mselectedApp.isEmpty();
    }

    //Getters
    public List<SummarizeSucc> getMlistApp() {
        return mlistApp;
    }

    public SummarizeSucc getMtmpApp() {
        return mtmpApp;
    }

    //Setters
    public void setMtmpApp(SummarizeSucc mtmpApp) {
        this.mtmpApp = mtmpApp;
    }

    public List<SummarizeSucc> getMselectedApp() {
        return mselectedApp;
    }

    public void setMselectedApp(List<SummarizeSucc> mselectedApp) {
        this.mselectedApp = mselectedApp;
    }
    
    public String getLastAmountTotal() {
        long total = 0;

        for (SummarizeSucc sale : mlistApp) {
            total += sale.getAmount() * sale.getQuantity();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getPaymentAmountTotal() {
        long total = 0;

        for (SummarizeSucc sale : mlistApp) {
            total += Integer.parseInt(sale.getPayment_amount());
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getGrandAmountTotal() {
        long total = 0;

        for (SummarizeSucc sale : mlistApp) {
            total += Integer.parseInt(sale.getGrand_amount());
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public String getLastQuantityTotal() {
        int total = 0;

        for (SummarizeSucc sale : mlistApp) {
            total += sale.getQuantity();
        }

        return new DecimalFormat("###,###.###").format(total);
    }

    public StreamedContent exportFile() throws Exception {
        String templateFileName = strRealPath + File.separator + strFileTemplate;
        String fileName = strPrefexFileName + DateFormatUtils.format(new Date(), "yyyyMMdd") + ".xls";
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

    public List<ProfilePartner> getMlistProfilePartner() {
        return mlistProfilePartner;
    }

    public void setMlistProfilePartner(List<ProfilePartner> mlistProfilePartner) {
        this.mlistProfilePartner = mlistProfilePartner;
    }

    public String getmProfileID() {
        return mProfileID;
    }

    public void setmProfileID(String mProfileID) {
        this.mProfileID = mProfileID;
    }

    public Date getMdStartDate() {
        return mdStartDate;
    }

    public void setMdStartDate(Date mdStartDate) {
        this.mdStartDate = mdStartDate;
    }

    public Date getMdEndDate() {
        return mdEndDate;
    }

    public void setMdEndDate(Date mdEndDate) {
        this.mdEndDate = mdEndDate;
    }
}
